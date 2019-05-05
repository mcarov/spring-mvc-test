package ru.itpark.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itpark.domain.Movie;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MovieRepository {
    private final NamedParameterJdbcTemplate template;

    public MovieRepository(DataSource source) {
        this.template = new NamedParameterJdbcTemplate(source);
    }

    public List<Movie> getAll() {
        return null;
    }

    public void save(Movie movie) {

    }

    public void removeById(long id) {

    }
}
