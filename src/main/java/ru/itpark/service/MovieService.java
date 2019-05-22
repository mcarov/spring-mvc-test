package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itpark.domain.*;
import ru.itpark.repository.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static ru.itpark.Constants.LIST_SIZE;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieKeywordIdRepository movieKeywordIdRepository;
    private final MovieGenreIdRepository movieGenreIdRepository;
    private final MovieCompanyIdRepository movieCompanyIdRepository;
    private final MovieCountryIdRepository movieCountryIdRepository;
    private final MovieLanguageIdRepository movieLanguageIdRepository;
    private final KeywordRepository keywordRepository;
    private final GenreRepository genreRepository;
    private final CompanyRepository companyRepository;
    private final CountryRepository countryRepository;
    private final LanguageRepository languageRepository;
    private final FileService fileService;

    public long getMovieRepoSize() {
        return movieRepository.size();
    }

    public long getKeywordRepoSize() {
        return keywordRepository.size();
    }

    public long getCompanyRepoSize() {
        return companyRepository.size();
    }

    public Movie getMovieById(long id) {
        Movie movie = movieRepository.getMovieById(id);
        addDataFromAdditionalTables(movie);
        return movie;
    }

    public List<Movie> getMoviesTop20() {
        return movieRepository.getMoviesTop20();
    }

    public List<Movie> getMovies(int number) {
        int offset = LIST_SIZE*(number-1);
        return movieRepository.getMovies(offset, LIST_SIZE);
    }

    public List<Keyword> getCollections(int number) {
        int offset = LIST_SIZE*(number-1);
        return keywordRepository.getKeywords(offset);
    }

    public List<Genre> getGenres() {
        return genreRepository.getGenres(0);
    }

    public List<Movie> getMoviesByCollectionId(long id) {
        List<Long> movieIds = movieKeywordIdRepository.getMovieIdsByKeywordId(id);
        return getMoviesSortedList(movieIds, Comparator.comparing(Movie::getPopularity), 0);
    }

    public List<Movie> getMoviesByGenreId(long id) {
        List<Long> movieIds = movieGenreIdRepository.getMovieIdsByGenreId(id);
        return getMoviesSortedList(movieIds, Comparator.comparing(Movie::getPopularity), 20);
    }

    public List<Movie> getMoviesByCompanyId(long id) {
        List<Long> movieIds = movieCompanyIdRepository.getMovieIdsByCompanyId(id);
        return getMoviesSortedList(movieIds, Comparator.comparing(Movie::getReleaseDate), 0);
    }

    public List<ProductionCompany> getCompanies(int number) {
        int offset = LIST_SIZE*(number-1);
        return companyRepository.getCompanies(offset);
    }

    public Genre getGenreById(long id) {
        return genreRepository.getGenreById(id);
    }

    public Keyword getKeywordById(long id) {
        return keywordRepository.getKeywordById(id);
    }

    public ProductionCompany getCompanyById(long id) {
        return companyRepository.getCompanyById(id);
    }

    public void updateDatabaseFromFile(MultipartFile file) throws IOException {
        Map<Long, Movie> movieMap = fileService.importFromCsvFile(file).stream().
                collect(Collectors.toMap(Movie::getId, movie -> movie, (key1, key2) -> key1));
        movieMap.forEach((id, movie) -> movieRepository.saveMovie(movie));

        saveDataInAdditionalTables(movieMap.values());
    }

    public void saveMovie(Movie movie) {
        searchIdsAndIsoCodes(movie);
        movieRepository.saveMovie(movie);
        saveDataInAdditionalTables(Collections.singletonList(movie));
    }

    public void removeMovieById(long id) {
        movieRepository.removeById(id);
        long count = 0;

        List<Long> keywordIds = movieKeywordIdRepository.getKeywordIdsByMovieId(id);
        movieKeywordIdRepository.removeKeywordIdsByMovieId(id);
        for(Long keywordId : keywordIds) {
            count = movieKeywordIdRepository.countKeywordId(keywordId);
            if(count == 0)
                keywordRepository.removeKeywordById(keywordId);
        }

        List<Long> genreIds = movieGenreIdRepository.getGenreIdsByMovieId(id);
        movieGenreIdRepository.removeGenreIdsByMovieId(id);
        for(Long genreId : genreIds) {
            count = movieGenreIdRepository.countGenreId(genreId);
            if(count == 0)
                genreRepository.removeGenreById(genreId);
        }

        List<Long> companyIds = movieCompanyIdRepository.getCompanyIdsByMovieId(id);
        movieCompanyIdRepository.removeCompanyIdsByMovieId(id);
        for(Long companyId : companyIds) {
            count = movieCompanyIdRepository.countCompanyId(companyId);
            if(count == 0)
                companyRepository.removeCompanyById(companyId);
        }

        List<String> countryIsoCodes = movieCountryIdRepository.getCountryIsoCodesByMovieId(id);
        movieCountryIdRepository.removeCountryIsoCodesByMovieId(id);
        for(String isoCode : countryIsoCodes) {
            count = movieCountryIdRepository.countContryIsoCode(isoCode);
            if(count == 0)
                countryRepository.removeCountryByIsoCode(isoCode);
        }

        List<String> languageIsoCodes = movieLanguageIdRepository.getLanguageIsoCodesByMovieId(id);
        movieLanguageIdRepository.removeLanguageIsoCodesByMovieId(id);
        for(String isoCode : languageIsoCodes) {
            count = movieLanguageIdRepository.countLanguageIsoCode(isoCode);
            if(count == 0)
                languageRepository.removeLanguageByIsoCode(isoCode);
        }
    }

    private void searchIdsAndIsoCodes(Movie movie) {
        Arrays.asList(movie.getKeywords()).
                forEach(keyword -> keyword.setId(keywordRepository.getKeywordIdByName(keyword.getName())));
        Arrays.asList(movie.getGenres()).
                forEach(genre -> genre.setId(genreRepository.getGenreIdByName(genre.getName())));
        Arrays.asList(movie.getProductionCompanies()).
                forEach(company -> company.setId(companyRepository.getCompanyIdByName(company.getName())));
        Arrays.asList(movie.getProductionCountries()).
                forEach(country -> {
                    if(country.getIso_3166_1().equals(""))
                        country.setIso_3166_1(countryRepository.getCountryIsoCodeByName(country.getName()));
                });
        Arrays.asList(movie.getSpokenLanguages()).
                forEach(language -> {
                    if(language.getIso_639_1().equals(""))
                        language.setIso_639_1(languageRepository.getLanguageIsoCodeByName(language.getName()));
                });
    }

    private void saveDataInAdditionalTables(Collection<Movie> collection) {
        List<Keyword> keywordList = collection.stream().
                map(Movie::getKeywords).
                flatMap(Arrays::stream).
                collect(Collectors.toList());
        keywordList.forEach(keywordRepository::saveKeyword);
        Map<Long, Keyword[]> keywordMap = collection.stream().
                collect(Collectors.toMap(Movie::getId, Movie::getKeywords));
        for(Map.Entry<Long, Keyword[]> entry : keywordMap.entrySet()) {
            Keyword[] keywords = entry.getValue();
            for(Keyword keyword : keywords)
                movieKeywordIdRepository.save(entry.getKey(), keyword.getId());
        }

        List<Genre> genreList = collection.stream().
                map(Movie::getGenres).
                flatMap(Arrays::stream).
                collect(Collectors.toList());
        genreList.forEach(genreRepository::saveGenre);
        Map<Long, Genre[]> genreMap = collection.stream().
                collect(Collectors.toMap(Movie::getId, Movie::getGenres));
        for(Map.Entry<Long, Genre[]> entry : genreMap.entrySet()) {
            Genre[] genres = entry.getValue();
            for(Genre genre : genres)
                movieGenreIdRepository.save(entry.getKey(), genre.getId());
        }

        List<ProductionCompany> companyList = collection.stream().
                map(Movie::getProductionCompanies).
                flatMap(Arrays::stream).
                collect(Collectors.toList());
        companyList.forEach(companyRepository::saveCompany);
        Map<Long, ProductionCompany[]> companyMap = collection.stream().
                collect(Collectors.toMap(Movie::getId, Movie::getProductionCompanies));
        for(Map.Entry<Long, ProductionCompany[]> entry : companyMap.entrySet()) {
            ProductionCompany[] companies = entry.getValue();
            for(ProductionCompany company : companies) {
                movieCompanyIdRepository.save(entry.getKey(), company.getId());
            }
        }

        List<ProductionCountry> countryList = collection.stream().
                map(Movie::getProductionCountries).
                flatMap(Arrays::stream).
                collect(Collectors.toList());
        countryList.forEach(countryRepository::saveCountry);
        Map<Long, ProductionCountry[]> countryMap = collection.stream().
                collect(Collectors.toMap(Movie::getId, Movie::getProductionCountries));
        for(Map.Entry<Long, ProductionCountry[]> entry : countryMap.entrySet()) {
            ProductionCountry[] countries = entry.getValue();
            for(ProductionCountry country : countries) {
                movieCountryIdRepository.save(entry.getKey(), country.getIso_3166_1());
            }
        }

        List<SpokenLanguage> languageList = collection.stream().
                map(Movie::getSpokenLanguages).
                flatMap(Arrays::stream).
                collect(Collectors.toList());
        languageList.forEach(languageRepository::saveLanguage);
        Map<Long, SpokenLanguage[]> languageMap = collection.stream().
                collect(Collectors.toMap(Movie::getId, Movie::getSpokenLanguages));
        for(Map.Entry<Long, SpokenLanguage[]> entry : languageMap.entrySet()) {
            SpokenLanguage[] languages = entry.getValue();
            for(SpokenLanguage language : languages) {
                movieLanguageIdRepository.save(entry.getKey(), language.getIso_639_1());
            }
        }
    }

    private List<Movie> getMoviesSortedList(List<Long> ids, Comparator comparator, int limit) {
        List<Movie> movies = ids.stream().map(movieRepository::getMovieById).collect(Collectors.toList());
        movies.forEach(this::addDataFromAdditionalTables);
        movies.sort(comparator.reversed());
        if(limit > movies.size()) limit = movies.size();
        return limit > 0 ? movies.subList(0, limit) : movies;
    }

    private void addDataFromAdditionalTables(Movie movie) {
        List<Long> keywordIds = movieKeywordIdRepository.getKeywordIdsByMovieId(movie.getId());
        Keyword[] keywords = keywordIds.stream().map(keywordRepository::getKeywordById).collect(Collectors.toList()).toArray(Keyword[]::new);

        List<Long> genreIds = movieGenreIdRepository.getGenreIdsByMovieId(movie.getId());
        Genre[] genres = genreIds.stream().map(genreRepository::getGenreById).collect(Collectors.toList()).toArray(Genre[]::new);

        List<Long> companyIds = movieCompanyIdRepository.getCompanyIdsByMovieId(movie.getId());
        ProductionCompany[] companies = companyIds.stream().map(companyRepository::getCompanyById).collect(Collectors.toList()).toArray(ProductionCompany[]::new);

        List<String> countryIsoCodes = movieCountryIdRepository.getCountryIsoCodesByMovieId(movie.getId());
        ProductionCountry[] countries = countryIsoCodes.stream().map(countryRepository::getCountryByIsoCode).collect(Collectors.toList()).toArray(ProductionCountry[]::new);

        List<String> languageIsoCodes = movieLanguageIdRepository.getLanguageIsoCodesByMovieId(movie.getId());
        SpokenLanguage[] languages = languageIsoCodes.stream().map(languageRepository::getLanguageByIsoCode).collect(Collectors.toList()).toArray(SpokenLanguage[]::new);

        movie.setKeywords(keywords);
        movie.setGenres(genres);
        movie.setProductionCompanies(companies);
        movie.setProductionCountries(countries);
        movie.setSpokenLanguages(languages);
    }
}
