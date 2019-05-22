package ru.itpark.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class MovieGenreIdRepository {
    private final NamedParameterJdbcTemplate template;

    public MovieGenreIdRepository(DataSource source) {
        this.template = new NamedParameterJdbcTemplate(source);
    }

    @PostConstruct
    public void init() {
        template.getJdbcTemplate().execute("CREATE TABLE IF NOT EXISTS movie_genre(" +
                "movie_id INTEGER, " +
                "genre_id INTEGER, " +
                "PRIMARY KEY (movie_id, genre_id))");
    }

    public long countGenreId(long id) {
        Optional<Long> count = Optional.ofNullable(template.getJdbcTemplate().queryForObject(
                "SELECT COUNT(*) FROM movie_genre WHERE genre_id = ?", new Long[]{id}, Long.class));
        return count.orElse(0L);
    }

    public void save(long movieId, long genreId) {
        template.update("INSERT INTO movie_genre (movie_id, genre_id) VALUES (:movieId, :genreId) " +
                        "ON CONFLICT (movie_id, genre_id) DO UPDATE SET movie_id = :movieId, genre_id = :genreId",
                Map.of("movieId", movieId, "genreId", genreId));
    }

    public List<Long> getGenreIdsByMovieId(long id) {
        return template.query("SELECT genre_id FROM movie_genre WHERE movie_id = :id",
                Map.of("id", id), (resultSet, i) -> resultSet.getLong(1));
    }

    public List<Long> getMovieIdsByGenreId(long id) {
        return template.query("SELECT movie_id FROM movie_genre WHERE genre_id = :id",
                Map.of("id", id), (resultSet, i) -> resultSet.getLong(1));
    }

    public void removeGenreIdsByMovieId(long id) {
        template.update("DELETE FROM movie_genre WHERE movie_id = :id", Map.of("id", id));
    }
}
