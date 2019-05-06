package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.itpark.domain.Movie;
import ru.itpark.repository.MovieRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MessageSource source;
    private final MovieRepository repo;
    private final CsvFileService fileService;

    public List<Movie> getTop20() {
        return null;
    }

    public List<Movie> getAll() {
        return null;
    }

    public void save() {

    }

    public void remove() {

    }
}
