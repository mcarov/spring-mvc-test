package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itpark.domain.Genre;
import ru.itpark.repository.GenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public List<Genre> getGenres() {
        return genreRepository.getGenres(0);
    }

    public Genre getGenreById(long id) {
        return genreRepository.getGenreById(id);
    }

    public long getGenreIdByName(String name) {
        return genreRepository.getGenreIdByName(name);
    }

    public void saveGenre(Genre genre) {
        genreRepository.saveGenre(genre);
    }

    public void removeGenreById(long id) {
        genreRepository.removeGenreById(id);
    }
}
