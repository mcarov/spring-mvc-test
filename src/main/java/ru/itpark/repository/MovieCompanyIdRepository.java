package ru.itpark.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
public class MovieCompanyIdRepository {
    private final NamedParameterJdbcTemplate template;

    public MovieCompanyIdRepository(DataSource source) {
        this.template = new NamedParameterJdbcTemplate(source);
    }

    @PostConstruct
    public void init() {
        template.getJdbcTemplate().execute("CREATE TABLE IF NOT EXISTS movie_company(" +
                "movie_id INTEGER, company_id INTEGER)");
    }

    public void save(long movieId, long companyId) {
        template.update("INSERT INTO movie_company (movie_id, company_id) VALUES (:movieId, :companyId)",
                Map.of("movieId", movieId, "companyId", companyId));
    }

    public List<Long> getCompanyIdsByMovieId(long id) {
        return template.query("SELECT company_id FROM movie_company WHERE movie_id = :id",
                Map.of("id", id), (resultSet, i) -> resultSet.getLong(1));
    }

    public List<Long> getMovieIdsByCompanyId(long id) {
        return template.query("SELECT movie_id FROM movie_company WHERE company_id = :id",
                Map.of("id", id), (resultSet, i) -> resultSet.getLong(1));
    }

    public void removeCompanyIdsByMovieId(long id) {
        template.update("DELETE FROM movie_company WHERE movie_id = :id", Map.of("id", id));
    }
}
