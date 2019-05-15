package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itpark.domain.Genre;
import ru.itpark.domain.Keyword;
import ru.itpark.domain.Movie;
import ru.itpark.domain.ProductionCompany;
import ru.itpark.repository.MovieRepository;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static ru.itpark.Constants.LIST_SIZE;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository repository;
    private final FileService fileService;

    public int getRepositorySize() {
        return repository.size();
    }

    public Movie getById(long id) {
        return repository.getById(id);
    }

    public List<Movie> getTop20() {
        return repository.getTop20();
    }

    public List<Movie> getList(int number) {
        int offset = LIST_SIZE*(number-1);
        return repository.getList(offset);
    }

    public List<Movie> getTop20OfGenre(long id) {
        return repository.getTop20OfGenre(id);
    }

    public List<Movie> getListOfCompany(long id) {
        return repository.getListOfCompany(id);
    }

    public List<Movie> getListOfCollection(long id) {
        return repository.getListOfCollection(id);
    }

    public List<Genre> getGenres() {
        TreeMap<Long, Genre> map = new TreeMap<>();
        map.putAll(repository.getGenres().stream().
                flatMap(Arrays::stream).
                collect(Collectors.toMap(Genre::getId, genre -> genre, (key1, key2) -> key2)));

        return new ArrayList<>(map.values());
    }

    public List<ProductionCompany> getCompanies() {
        TreeMap<Long, ProductionCompany> map = new TreeMap<>();
        map.putAll(repository.getCompanies().stream().
                flatMap(Arrays::stream).
                collect(Collectors.toMap(ProductionCompany::getId, company -> company, (key1, key2) -> key2)));

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

    public void updateFromFile(MultipartFile file) throws IOException {
        List<Movie> movies = fileService.importFromCsvFile(file);
        for(Movie movie : movies) {
            repository.save(movie);
        }
    }

    public void remove() {

    }
}
