package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itpark.domain.Genre;
import ru.itpark.domain.Keyword;
import ru.itpark.domain.Movie;
import ru.itpark.domain.ProductionCompany;
import ru.itpark.repository.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.itpark.Constants.LIST_SIZE;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieGenreIdRepository movieGenreIdRepository;
    private final MovieKeywordIdRepository movieKeywordIdRepository;
    private final GenreRepository genreRepository;
    private final KeywordRepository keywordRepository;
    private final FileService fileService;

    public int getMovieRepoSize() {
        return movieRepository.size();
    }

    public int getGenreRepoSize() {
        return genreRepository.size();
    }

    public int getKeywordRepoSize() {
        return keywordRepository.size();
    }

    public Movie getById(long id) {
        Movie movie = movieRepository.getById(id);
        fillMovie(movie);
        return movie;
    }

    public List<Movie> getTop20() {
        return movieRepository.getTop20();
    }

    public List<Movie> getMovies(int number) {
        int offset = LIST_SIZE*(number-1);
        return movieRepository.getMovies(offset, LIST_SIZE);
    }

    public List<Keyword> getCollections(int number) {
        int offset = LIST_SIZE*(number-1);
        return keywordRepository.getKeywords(offset, LIST_SIZE);
    }

    public List<Genre> getGenres() {
        return genreRepository.getGenres(0, LIST_SIZE);
    }

    public List<Movie> getMoviesByCollectionId(long id) {
        List<Long> movieIds = movieKeywordIdRepository.getMovieIdsByKeywordId(id);

        Map<Double, Movie> map = new TreeMap<>(Collections.reverseOrder());
        map.putAll(getMovieMap(movieIds.stream()));

        return new ArrayList<>(map.values());
    }

    public List<Movie> getTop20ByGenreId(long id) {
        List<Long> movieIds = movieGenreIdRepository.getMovieIdsByGenreId(id);

        Map<Double, Movie> map = new TreeMap<>(Collections.reverseOrder());
        map.putAll(getMovieMap(movieIds.stream()));

        return map.values().stream().limit(20).collect(Collectors.toList());
    }

    public List<Movie> getListOfCompany(long id) {
        return movieRepository.getListOfCompany(id);
    }

    public List<ProductionCompany> getCompanies() {
        TreeMap<Long, ProductionCompany> map = new TreeMap<>();
        map.putAll(movieRepository.getCompanies().stream().
                flatMap(Arrays::stream).
                collect(Collectors.toMap(ProductionCompany::getId, company -> company, (key1, key2) -> key2)));

        return new ArrayList<>(map.values());
    }

    public void updateFromFile(MultipartFile file) throws IOException {
        Map<Long, Movie> movieMap = fileService.importFromCsvFile(file).stream().
                collect(Collectors.toMap(Movie::getId, movie -> movie, (key1, key2) -> key1));
        movieMap.forEach((id, movie) -> movieRepository.save(movie));

        Map<Long, Genre> genreMap = movieMap.values().stream().
                map(Movie::getGenres).
                flatMap(Arrays::stream).
                collect(Collectors.toMap(Genre::getId, genre -> genre, (key1, key2) -> key1));
        genreMap.forEach((id, genre) -> genreRepository.save(genre));
        Map<Long, Genre[]> map1 = movieMap.values().stream().
                collect(Collectors.toMap(Movie::getId, Movie::getGenres));
        for(Map.Entry<Long, Genre[]> entry : map1.entrySet()) {
            Genre[] genres = entry.getValue();
            for(Genre genre : genres)
                movieGenreIdRepository.save(entry.getKey(), genre.getId());
        }

        Map<Long, Keyword> keywordMap = movieMap.values().stream().
                map(Movie::getKeywords).
                flatMap(Arrays::stream).
                collect(Collectors.toMap(Keyword::getId, keyword -> keyword, (key1, key2) -> key1));
        keywordMap.forEach((id, keyword) -> keywordRepository.save(keyword));

        Map<Long, Keyword[]> map2 = movieMap.values().stream().
                collect(Collectors.toMap(Movie::getId, Movie::getKeywords));
        for(Map.Entry<Long, Keyword[]> entry : map2.entrySet()) {
            Keyword[] keywords = entry.getValue();
            for(Keyword keyword : keywords)
                movieKeywordIdRepository.save(entry.getKey(), keyword.getId());
        }
    }

    public void save(Movie movie) {

    }

    public void remove() {

    }

    private Map<Double, Movie> getMovieMap(Stream<Long> stream) {
        Map<Double, Movie> movieMap = stream.map(movieRepository::getById).
                collect(Collectors.toMap(Movie::getPopularity, movie -> movie));
        for(Movie movie : movieMap.values()) {
            fillMovie(movie);
        }
        return movieMap;
    }

    private void fillMovie(Movie movie) {
        List<Long> genreIds = movieGenreIdRepository.getGenreIdsByMovieId(movie.getId());
        Genre[] genres = genreIds.stream().map(genreRepository::getGenreById).collect(Collectors.toList()).toArray(Genre[]::new);
        List<Long> keywordIds = movieKeywordIdRepository.getKeywordIdsByMovieId(movie.getId());
        Keyword[] keywords = keywordIds.stream().map(keywordRepository::getKeywordById).collect(Collectors.toList()).toArray(Keyword[]::new);

        movie.setGenres(genres);
        movie.setKeywords(keywords);
    }
}
