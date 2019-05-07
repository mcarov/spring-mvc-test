package ru.itpark.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itpark.domain.Movie;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
public class MovieRepository {
    private final NamedParameterJdbcTemplate template;
    private final RowMapper<Movie> rowMapper = (resultSet, i) -> {
        Movie movie = new Movie();
        movie.setBudget(resultSet.getLong(1));
        movie.setGenres(resultSet.getString(2));
        movie.setHomePage(resultSet.getString(3));
        movie.setId(resultSet.getLong(4));
        movie.setKeywords(resultSet.getString(5));
        movie.setOriginalLanguage(resultSet.getString(6));
        movie.setOriginalTitle(resultSet.getString(7));
        movie.setOverview(resultSet.getString(8));
        movie.setPopularity(resultSet.getDouble(9));
        movie.setProductionCompanies(resultSet.getString(10));
        movie.setProductionCountries(resultSet.getString(11));
        movie.setReleaseDate(resultSet.getString(12));
        movie.setRevenue(resultSet.getLong(13));
        movie.setRuntime(resultSet.getInt(14));
        movie.setSpokenLanguages(resultSet.getString(15));
        movie.setStatus(resultSet.getString(16));
        movie.setTagline(resultSet.getString(17));
        movie.setTitle(resultSet.getString(18));
        movie.setVoteAverage(resultSet.getDouble(19));
        movie.setVoteCount(resultSet.getLong(20));
        return movie;
    };

    public MovieRepository(DataSource source) {
        template = new NamedParameterJdbcTemplate(source);
    }

    @PostConstruct
    public void init() {
        template.getJdbcTemplate().execute("CREATE TABLE IF NOT EXISTS movies(" +
                "budget INTEGER, " +
                "genres TEXT, " +
                "homepage TEXT, " +
                "id INTEGER, " +
                "keywords TEXT, " +
                "original_language TEXT, " +
                "original_title TEXT, " +
                "overview TEXT, " +
                "popularity REAL, " +
                "production_companies TEXT, " +
                "production_countries TEXT, " +
                "release_date TEXT, " +
                "revenue INTEGER , " +
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
    public List<Movie> getTop20() {
        return template.query("SELECT * FROM movies ORDER BY popularity DESC LIMIT 20", rowMapper);
    }

    public void save(Movie movie) {
        template.update("INSERT INTO movies (" +
                "budget, " +
                "genres, " +
                "homepage, " +
                "id, " +
                "keywords, " +
                "original_language, " +
                "original_title, " +
                "overview, " +
                "popularity, " +
                "production_companies, " +
                "production_countries, " +
                "release_date, " +
                "revenue, " +
                "runtime, " +
                "spoken_languages, " +
                "status, " +
                "tagline, " +
                "title, " +
                "vote_average, " +
                "vote_count) " +
                "VALUES (:budget, :genres, :homepage, :id, :keywords, :original_language, :original_title, " +
                ":overview, :popularity, :production_companies, :production_countries, :release_date, :revenue, " +
                ":runtime, :spoken_languages, :status, :tagline, :title, :vote_average, :vote_count)", getParamMap(movie));

    }

    public void removeById(long id) {

    }

    private Map<String, ?> getParamMap(Movie movie) {
        return Map.ofEntries(entry("budget", movie.getBudget()),
                entry("genres", movie.getGenres()),
                entry("homepage", movie.getHomePage()),
                entry("id", movie.getId()),
                entry("keywords", movie.getKeywords()),
                entry("original_language", movie.getOriginalLanguage()),
                entry("original_title", movie.getOriginalTitle()),
                entry("overview", movie.getOverview()),
                entry("popularity", movie.getPopularity()),
                entry("production_companies", movie.getProductionCompanies()),
                entry("production_countries", movie.getProductionCompanies()),
                entry("release_date", movie.getReleaseDate()),
                entry("revenue", movie.getRevenue()),
                entry("runtime", movie.getRuntime()),
                entry("spoken_languages", movie.getSpokenLanguages()),
                entry("status", movie.getStatus()),
                entry("tagline", movie.getTagline()),
                entry("title", movie.getTitle()),
                entry("vote_average", movie.getVoteAverage()),
                entry("vote_count", movie.getVoteCount())
               );
    }
}
