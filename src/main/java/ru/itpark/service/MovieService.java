package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itpark.domain.Genre;
import ru.itpark.domain.Keyword;
import ru.itpark.domain.Movie;
import ru.itpark.domain.ProductionCompany;
import ru.itpark.repository.GenreRepository;
import ru.itpark.repository.GenreMovieIdRepository;
import ru.itpark.repository.MovieRepository;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static ru.itpark.Constants.LIST_SIZE;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final GenreMovieIdRepository genreMovieIdRepository;
    private final FileService fileService;

    public int getMovieRepoSize() {
        return movieRepository.size();
    }

    public int getGenreRepoSize() {
        return genreRepository.size();
    }

    public Movie getById(long id) {
        Movie movie = movieRepository.getById(id);

        List<Long> genreIds = genreMovieIdRepository.getGenreIdsByMovieId(id);
        Genre[] genres = genreIds.stream().map(genreRepository::getGenreById).collect(Collectors.toList()).toArray(Genre[]::new);
        movie.setGenres(genres);

        return movie;
    }

    public List<Movie> getTop20() {
        return movieRepository.getTop20();
    }

    public List<Movie> getMovies(int number) {
        int offset = LIST_SIZE*(number-1);
        return movieRepository.getList(offset);
    }

    public List<Movie> getTop20OfGenre(long id) {
        return movieRepository.getTop20OfGenre(id);
    }

    public List<Movie> getListOfCompany(long id) {
        return movieRepository.getListOfCompany(id);
    }

    public List<Movie> getListOfCollection(long id) {
        return movieRepository.getListOfCollection(id);
    }

    public List<Genre> getGenres() {
        return genreRepository.getGenres();
    }

    public List<ProductionCompany> getCompanies() {
        TreeMap<Long, ProductionCompany> map = new TreeMap<>();
        map.putAll(movieRepository.getCompanies().stream().
                flatMap(Arrays::stream).
                collect(Collectors.toMap(ProductionCompany::getId, company -> company, (key1, key2) -> key2)));

        return new ArrayList<>(map.values());
    }

    public List<Keyword> getCollections() {
        List<Keyword> list = new ArrayList<>();
        movieRepository.getCollections().forEach(collections -> list.addAll(Arrays.asList(collections)));

        TreeMap<Long, Keyword> map = new TreeMap<>();
        map.putAll(list.stream().collect(Collectors.toMap(Keyword::getId, collection -> collection, (key1, key2) -> key2)));

        return new ArrayList<>(map.values());
    }

    public void save(Movie movie) {

    }

    public void updateFromFile(MultipartFile file) throws IOException {
        List<Movie> movieList = fileService.importFromCsvFile(file);

        Map<Long, Movie> movieMap = movieList.stream().
                collect(Collectors.toMap(Movie::getId, movie -> movie, (key1, key2) -> key1));
        movieMap.forEach((id, movie) -> movieRepository.save(movie));

        Map<Long, Genre> genreMap = movieMap.values().stream().
                map(Movie::getGenres).
                flatMap(Arrays::stream).
                collect(Collectors.toMap(Genre::getId, genre -> genre, (key1, key2) -> key1));
        genreMap.forEach((id, genre) -> genreRepository.save(genre));

        Map<Long, Genre[]> map = movieMap.values().stream().
                collect(Collectors.toMap(Movie::getId, Movie::getGenres));
        for(Map.Entry<Long, Genre[]> entry : map.entrySet()) {
            Genre[] genres = entry.getValue();
            for(Genre genre : genres)
                genreMovieIdRepository.save(genre.getId(), entry.getKey());
        }
    }

    public void remove() {

    }
}
