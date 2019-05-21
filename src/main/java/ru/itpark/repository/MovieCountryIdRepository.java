package ru.itpark.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class MovieCountryIdRepository {
    private final NamedParameterJdbcTemplate template;

    public MovieCountryIdRepository(DataSource source) {
        this.template = new NamedParameterJdbcTemplate(source);
    }

    @PostConstruct
    public void init() {
        template.getJdbcTemplate().execute("CREATE TABLE IF NOT EXISTS movie_country(" +
                "movie_id INTEGER, country_iso_code TEXT)");
    }

    public long countContryIsoCode(String isoCode) {
        Optional<Long> count = Optional.ofNullable(template.getJdbcTemplate().queryForObject(
                "SELECT COUNT(*) FROM movie_country WHERE country_iso_code LIKE ?", new String[]{isoCode}, Long.class));
        return count.orElse(0L);
    }

    public void save(long movieId, String isoCode) {
        template.update("INSERT INTO movie_country (movie_id, country_iso_code) VALUES (:movieId, :isoCode)",
                Map.of("movieId", movieId, "isoCode", isoCode));
    }

    public List<String> getCountryIsoCodesByMovieId(long id) {
        return template.query("SELECT country_iso_code FROM movie_country WHERE movie_id = :id",
                Map.of("id", id), (resultSet, i) -> resultSet.getString(1));
    }

    public void removeCountryIsoCodesByMovieId(long id) {
        template.update("DELETE FROM movie_country WHERE movie_id = :id", Map.of("id", id));
    }
}
