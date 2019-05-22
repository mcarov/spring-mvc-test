package ru.itpark.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class MovieKeywordIdRepository {
    private final NamedParameterJdbcTemplate template;

    public MovieKeywordIdRepository(DataSource source) {
        this.template = new NamedParameterJdbcTemplate(source);
    }

    @PostConstruct
    public void init() {
        template.getJdbcTemplate().execute("CREATE TABLE IF NOT EXISTS movie_keyword(" +
                "movie_id INTEGER, " +
                "keyword_id INTEGER, " +
                "PRIMARY KEY (movie_id, keyword_id))");
    }

    public long countKeywordId(long id) {
        Optional<Long> count = Optional.ofNullable(template.getJdbcTemplate().queryForObject(
                "SELECT COUNT(*) FROM movie_keyword WHERE keyword_id = ?", new Long[]{id}, Long.class));
        return count.orElse(0L);
    }

    public void save(long movieId, long keywordId) {
        template.update("INSERT INTO movie_keyword (movie_id, keyword_id) VALUES (:movieId, :keywordId) " +
                        "ON CONFLICT (movie_id, keyword_id) DO UPDATE SET movie_id = :movieId, keyword_id = :keywordId",
                Map.of("movieId", movieId, "keywordId", keywordId));
    }

    public List<Long> getKeywordIdsByMovieId(long id) {
        return template.query("SELECT keyword_id FROM movie_keyword WHERE movie_id = :id",
                Map.of("id", id), (resultSet, i) -> resultSet.getLong(1));
    }

    public List<Long> getMovieIdsByKeywordId(long id) {
        return template.query("SELECT movie_id FROM movie_keyword WHERE keyword_id = :id",
                Map.of("id", id), (resultSet, i) -> resultSet.getLong(1));
    }

    public void removeKeywordIdsByMovieId(long id) {
        template.update("DELETE FROM movie_keyword WHERE movie_id = :id", Map.of("id", id));
    }
}
