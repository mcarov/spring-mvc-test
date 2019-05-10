package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.itpark.domain.Movie;
import ru.itpark.repository.MovieRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MessageSource source;
    private final MovieRepository repository;
    private final CsvFileService fileService;
    private List<Movie> movies = new ArrayList<>(4803);

    public List<Movie> getTop20() {
        return repository.getTop20();
    }

    public Movie getById(long id) {
        return repository.getById(id);
    }

    public List<Movie> getAll() {
        return null;
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
