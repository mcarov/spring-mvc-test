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
        fillMovie(movie);
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

    public void updateDatabaseFromFile(MultipartFile file) throws IOException {
        Map<Long, Movie> movieMap = fileService.importFromCsvFile(file).stream().
                collect(Collectors.toMap(Movie::getId, movie -> movie, (key1, key2) -> key1));
        movieMap.forEach((id, movie) -> movieRepository.saveMovie(movie));

        saveDataInAdditionalTables(movieMap.values());
    }

    public void saveMovie(Movie movie) {
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

    private void saveDataInAdditionalTables(Collection<Movie> collection) {
        Map<Long, Keyword> keywordMap = collection.stream().
                map(Movie::getKeywords).
                flatMap(Arrays::stream).
                collect(Collectors.toMap(Keyword::getId, keyword -> keyword, (key1, key2) -> key1));
        keywordMap.forEach((id, keyword) -> keywordRepository.save(keyword));
        Map<Long, Keyword[]> map1 = collection.stream().
                collect(Collectors.toMap(Movie::getId, Movie::getKeywords));
        for(Map.Entry<Long, Keyword[]> entry : map1.entrySet()) {
            Keyword[] keywords = entry.getValue();
            for(Keyword keyword : keywords)
                movieKeywordIdRepository.save(entry.getKey(), keyword.getId());
        }

        Map<Long, Genre> genreMap = collection.stream().
                map(Movie::getGenres).
                flatMap(Arrays::stream).
                collect(Collectors.toMap(Genre::getId, genre -> genre, (key1, key2) -> key1));
        genreMap.forEach((id, genre) -> genreRepository.save(genre));
        Map<Long, Genre[]> map2 = collection.stream().
                collect(Collectors.toMap(Movie::getId, Movie::getGenres));
        for(Map.Entry<Long, Genre[]> entry : map2.entrySet()) {
            Genre[] genres = entry.getValue();
            for(Genre genre : genres)
                movieGenreIdRepository.save(entry.getKey(), genre.getId());
        }

        Map<Long, ProductionCompany> companyMap = collection.stream().
                map(Movie::getProductionCompanies).
                flatMap(Arrays::stream).
                collect(Collectors.toMap(ProductionCompany::getId, company -> company, (key1, key2) -> key1));
        companyMap.forEach((id, company) -> companyRepository.save(company));
        Map<Long, ProductionCompany[]> map3 = collection.stream().collect(Collectors.toMap(Movie::getId, Movie::getProductionCompanies));
        for(Map.Entry<Long, ProductionCompany[]> entry : map3.entrySet()) {
            ProductionCompany[] companies = entry.getValue();
            for(ProductionCompany company : companies) {
                movieCompanyIdRepository.save(entry.getKey(), company.getId());
            }
        }

        Map<String, ProductionCountry> countryMap = collection.stream().
                map(Movie::getProductionCountries).
                flatMap(Arrays::stream).
                collect(Collectors.toMap(ProductionCountry::getIso_3166_1, country -> country, (key1, key2) -> key1));
        countryMap.forEach((isoCode, country) -> countryRepository.save(country));
        Map<Long, ProductionCountry[]> map4 = collection.stream().collect(Collectors.toMap(Movie::getId, Movie::getProductionCountries));
        for(Map.Entry<Long, ProductionCountry[]> entry : map4.entrySet()) {
            ProductionCountry[] countries = entry.getValue();
            for(ProductionCountry country : countries) {
                movieCountryIdRepository.save(entry.getKey(), country.getIso_3166_1());
            }
        }

        Map<String, SpokenLanguage> languageMap = collection.stream().
                map(Movie::getSpokenLanguages).
                flatMap(Arrays::stream).
                collect(Collectors.toMap(SpokenLanguage::getIso_639_1, language -> language, (key1, key2) -> key1));
        languageMap.forEach((isoCode, language) -> languageRepository.save(language));
        Map<Long, SpokenLanguage[]> map5 = collection.stream().collect(Collectors.toMap(Movie::getId, Movie::getSpokenLanguages));
        for(Map.Entry<Long, SpokenLanguage[]> entry : map5.entrySet()) {
            SpokenLanguage[] languages = entry.getValue();
            for(SpokenLanguage language : languages) {
                movieLanguageIdRepository.save(entry.getKey(), language.getIso_639_1());
            }
        }
    }

    private List<Movie> getMoviesSortedList(List<Long> ids, Comparator comparator, int limit) {
        List<Movie> movies = ids.stream().map(movieRepository::getMovieById).collect(Collectors.toList());
        movies.forEach(this::fillMovie);
        movies.sort(comparator.reversed());
        return limit > 0 ? movies.subList(0, limit) : movies;
    }

    private void fillMovie(Movie movie) {
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
