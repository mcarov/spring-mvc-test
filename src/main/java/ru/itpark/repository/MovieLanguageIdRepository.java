package ru.itpark.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class MovieLanguageIdRepository {
    private final NamedParameterJdbcTemplate template;

    public MovieLanguageIdRepository(DataSource source) {
        this.template = new NamedParameterJdbcTemplate(source);
    }

    @PostConstruct
    public void init() {
        template.getJdbcTemplate().execute("CREATE TABLE IF NOT EXISTS movie_language(" +
                "movie_id INTEGER, language_iso_code TEXT)");
    }

    public long countLanguageIsoCode(String isoCode) {
        Optional<Long> count = Optional.ofNullable(template.getJdbcTemplate().queryForObject(
                "SELECT COUNT(*) FROM movie_language WHERE language_iso_code LIKE ?", new String[]{isoCode}, Long.class));
        return count.orElse(0L);
    }

    public void save(long movieId, String isoCode) {
        template.update("INSERT INTO movie_language (movie_id, language_iso_code) VALUES (:movieId, :isoCode)",
                Map.of("movieId", movieId, "isoCode", isoCode));
    }

    public List<String> getLanguageIsoCodesByMovieId(long id) {
        return template.query("SELECT language_iso_code FROM movie_language WHERE movie_id = :id",
                Map.of("id", id), (resultSet, i) -> resultSet.getString(1));
    }

    public void removeLanguageIsoCodesByMovieId(long id) {
        template.update("DELETE FROM movie_language WHERE movie_id = :id", Map.of("id", id));
    }
}
