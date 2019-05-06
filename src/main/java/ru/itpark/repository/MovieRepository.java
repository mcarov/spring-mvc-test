package ru.itpark.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itpark.domain.Movie;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class MovieRepository {
    private final NamedParameterJdbcTemplate template;

    public MovieRepository(DataSource source) {
        this.template = new NamedParameterJdbcTemplate(source);
    }

    @PostConstruct
    public void init() {
        this.template.getJdbcTemplate().execute("CREATE TABLE IF NOT EXISTS movies(" +
                "budget INTEGER, " +
                "genres TEXT, " +
                "homepage TEXT, " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "keywords TEXT, " +
                "original_language TEXT, " +
                "original_title TEXT, " +
                "overview TEXT, " +
                "popularity REAL, " +
                "production_companies TEXT, " +
                "production_countries TEXT, " +
                "release_date TEXT, " +
                "runtime INTEGER, " +
                "spoken_languages TEXT, " +
                "status TEXT, " +
                "tagline TEXT, " +
                "title TEXT, " +
                "vote_average REAL, " +
                "vote_count INTEGER)");
    }

    public List<Movie> getAll() {
        return null;
    }

    public void save(Movie movie) {

    }

    public void removeById(long id) {

    }
}
