package ru.itpark.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itpark.domain.*;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
public class MovieRepository {
    private final NamedParameterJdbcTemplate template;

    private final RowMapper<Movie> fullRowMapper = (resultSet, i) -> {
        Movie movie = new Movie();
        movie.setBudget(resultSet.getLong(1));
        movie.setHomePage(resultSet.getString(2));
        movie.setId(resultSet.getLong(3));
        movie.setOriginalLanguage(resultSet.getString(4));
        movie.setOriginalTitle(resultSet.getString(5));
        movie.setOverview(resultSet.getString(6));
        movie.setPopularity(resultSet.getDouble(7));
        movie.setReleaseDate(Date.from(Instant.ofEpochSecond(resultSet.getLong(8))));
        movie.setRevenue(resultSet.getLong(9));
        movie.setRuntime(resultSet.getInt(10));
        movie.setStatus(resultSet.getString(11));
        movie.setTagline(resultSet.getString(12));
        movie.setTitle(resultSet.getString(13));
        movie.setVoteAverage(resultSet.getDouble(14));
        movie.setVoteCount(resultSet.getLong(15));
        return movie;
    };

    private final RowMapper<Movie> simpleRowMapper = (resultSet, i) -> {
        Movie movie = new Movie();
        movie.setId(resultSet.getLong(1));
        movie.setTitle(resultSet.getString(2));
        movie.setPopularity(resultSet.getDouble(3));
        movie.setReleaseDate(Date.from(Instant.ofEpochSecond(resultSet.getLong(4))));
        return movie;
    };

    public MovieRepository(DataSource source) {
        this.template = new NamedParameterJdbcTemplate(source);
    }

    @PostConstruct
    public void init() {
        template.getJdbcTemplate().execute("CREATE TABLE IF NOT EXISTS movies(" +
                "budget INTEGER, " +
                "homepage TEXT, " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "original_language TEXT, " +
                "original_title TEXT, " +
                "overview TEXT, " +
                "popularity REAL, " +
                "release_date INTEGER, " +
                "revenue INTEGER , " +
                "runtime INTEGER, " +
                "status TEXT, " +
                "tagline TEXT, " +
                "title TEXT, " +
                "vote_average REAL, " +
                "vote_count INTEGER)");
    }

    public int size() {
        try {
            return template.getJdbcTemplate().queryForObject("SELECT COUNT(*) FROM movies", Integer.class);
        }
        catch(NullPointerException e) {
            return 0;
        }
    }

    public Movie getMovieById(long id) {
       List<Movie> list = template.query("SELECT budget, " +
               "homepage, " +
               "id, " +
               "original_language, " +
               "original_title, " +
               "overview, " +
               "popularity, " +
               "release_date, " +
               "revenue, " +
               "runtime, " +
               "status, " +
               "tagline, " +
               "title, " +
               "vote_average, " +
               "vote_count FROM movies WHERE id = :id", Map.of("id", id), fullRowMapper);
       return list.get(0);
    }

    public List<Movie> getMoviesTop20() {
        return template.query(
                "SELECT id, " +
                        "title, " +
                        "popularity, " +
                        "release_date FROM movies ORDER BY popularity DESC LIMIT 20",
                simpleRowMapper);
    }

    public List<Movie> getMovies(int offset, int limit) {
        return template.query(
                "SELECT id, " +
                        "title, " +
                        "popularity, " +
                        "release_date FROM movies ORDER BY popularity DESC LIMIT :offset, :limit",
                Map.of("offset", offset, "limit", limit), simpleRowMapper);
    }

    public void saveMovie(Movie movie) {
        template.update("INSERT INTO movies (" +
                "budget, " +
                "homepage, " +
                "id, " +
                "original_language, " +
                "original_title, " +
                "overview, " +
                "popularity, " +
                "release_date, " +
                "revenue, " +
                "runtime, " +
                "status, " +
                "tagline, " +
                "title, " +
                "vote_average, " +
                "vote_count) " +
                "VALUES (:budget, :homepage, :id, :original_language, :original_title, " +
                ":overview, :popularity, :release_date, :revenue, :runtime," +
                ":status, :tagline, :title, :vote_average, :vote_count)", getParamMap(movie));
    }

    public void removeById(long id) {
        template.update("DELETE FROM movies WHERE id = :id", Map.of("id", id));
    }

    private Map<String, ?> getParamMap(Movie movie) {
        return Map.ofEntries(
                entry("budget", movie.getBudget()),
                entry("homepage", movie.getHomePage()),
                entry("id", movie.getId()),
                entry("original_language", movie.getOriginalLanguage()),
                entry("original_title", movie.getOriginalTitle()),
                entry("overview", movie.getOverview()),
                entry("popularity", movie.getPopularity()),
                entry("release_date",movie.getReleaseDate().getTime()/1000),
                entry("revenue", movie.getRevenue()),
                entry("runtime", movie.getRuntime()),
                entry("status", movie.getStatus()),
                entry("tagline", movie.getTagline()),
                entry("title", movie.getTitle()),
                entry("vote_average", movie.getVoteAverage()),
                entry("vote_count", movie.getVoteCount())
               );
    }
}
