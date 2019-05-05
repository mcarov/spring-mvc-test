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

    public List<Movie> getALl() {
        return null;
    }

    public void save() {

    }

    public void remove() {

    }
}
