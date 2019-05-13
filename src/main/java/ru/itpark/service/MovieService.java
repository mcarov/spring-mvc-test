package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.itpark.domain.Genre;
import ru.itpark.domain.Keyword;
import ru.itpark.domain.Movie;
import ru.itpark.domain.ProductionCompany;
import ru.itpark.repository.MovieRepository;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MessageSource source;
    private final MovieRepository repository;
    private final CsvFileService fileService;
    private List<Movie> movies = new ArrayList<>(5000);

    public int getRepositorySize() {
        return repository.size();
    }

    public Movie getById(long id) {
        return repository.getById(id);
    }

    public List<Movie> getTop20() {
        return repository.getTop20();
    }

    public List<Movie> getListOf50(int number) {
        int offset = 50*(number-1);
        return repository.getListOf50(offset);
    }

    public List<Movie> getTop20OfGenre(long id) {
        return repository.getTop20OfGenre(id);
    }

    public List<Movie> getMoviesOfCompany(long id) {
        return repository.getMoviesOfCompany(id);
    }

    public List<Movie> getMoviesOfCollection(long id) {
        return repository.getMoviesOfCollection(id);
    }

    public List<Genre> getGenres() {
        List<Genre> list = new ArrayList<>();
        repository.getGenres().forEach(genres -> list.addAll(Arrays.asList(genres)));

        TreeMap<Long, Genre> map = new TreeMap<>();
        map.putAll(list.stream().collect(Collectors.toMap(Genre::getId, genre -> genre, (key1, key2) -> key2)));

        return new ArrayList<>(map.values());
    }

    public List<ProductionCompany> getCompanies() {
        List<ProductionCompany> list = new ArrayList<>();
        repository.getCompanies().forEach(companies -> list.addAll(Arrays.asList(companies)));

        TreeMap<Long, ProductionCompany> map = new TreeMap<>();
        map.putAll(list.stream().collect(Collectors.toMap(ProductionCompany::getId, company -> company, (key1, key2) -> key2)));

        return new ArrayList<>(map.values());
    }

    public List<Keyword> getCollections() {
        List<Keyword> list = new ArrayList<>();
        repository.getCollections().forEach(collections -> list.addAll(Arrays.asList(collections)));

        TreeMap<Long, Keyword> map = new TreeMap<>();
        map.putAll(list.stream().collect(Collectors.toMap(Keyword::getId, collection -> collection, (key1, key2) -> key2)));

        return new ArrayList<>(map.values());
    }

    public void save(Movie movie) {

    }

    public void updateFromFile() throws IOException {
        if(movies.size() != 0) return;
        movies = fileService.importFromCsvFile("C:/Users/Dmitry/Desktop/Spring");
        for(Movie movie : movies) {
            repository.save(movie);
        }
    }

    public void remove() {

    }
}
