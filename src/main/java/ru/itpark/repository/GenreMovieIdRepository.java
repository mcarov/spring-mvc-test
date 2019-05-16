package ru.itpark.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
public class GenreMovieIdRepository {
    private final NamedParameterJdbcTemplate template;

    public GenreMovieIdRepository(DataSource source) {
        this.template = new NamedParameterJdbcTemplate(source);
    }

    @PostConstruct
    public void init() {
        template.getJdbcTemplate().execute("CREATE TABLE IF NOT EXISTS genre_movie(" +
                "genre_id INTEGER, movie_id INTEGER)");
    }

    public void save(long genreId, long movieId) {
        template.update("INSERT INTO genre_movie (genre_id, movie_id) VALUES (:genreId, :movieId)",
                Map.of("genreId", genreId, "movieId", movieId));
    }

    public List<Long> getGenreIdsByMovieId(long id) {
        return template.query("SELECT genre_id FROM genre_movie WHERE movie_id = :id",
                Map.of("id", id), (resultSet, i) -> resultSet.getLong(1));
    }
}
