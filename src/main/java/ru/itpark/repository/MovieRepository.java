package ru.itpark.repository;

import com.google.gson.Gson;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itpark.domain.*;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
public class MovieRepository {
    private Gson gson;

    private final NamedParameterJdbcTemplate template;
    private final RowMapper<Movie> fullRowMapper = (resultSet, i) -> {
        Movie movie = new Movie();
        movie.setBudget(resultSet.getLong(1));
        movie.setGenres(
                gson.fromJson(resultSet.getString(2), Genre[].class));
        movie.setHomePage(resultSet.getString(3));
        movie.setId(resultSet.getLong(4));
        movie.setKeywords(
                gson.fromJson(resultSet.getString(5), Keyword[].class));
        movie.setOriginalLanguage(resultSet.getString(6));
        movie.setOriginalTitle(resultSet.getString(7));
        movie.setOverview(resultSet.getString(8));
        movie.setPopularity(resultSet.getDouble(9));
        movie.setProductionCompanies(
                gson.fromJson(resultSet.getString(10), ProductionCompany[].class));
        movie.setProductionCountries(
                gson.fromJson(resultSet.getString(11), ProductionCountry[].class));
        movie.setReleaseDate(resultSet.getString(12));
        movie.setRevenue(resultSet.getLong(13));
        movie.setRuntime(resultSet.getInt(14));
        movie.setSpokenLanguages(
                gson.fromJson(resultSet.getString(15), SpokenLanguage[].class));
        movie.setStatus(resultSet.getString(16));
        movie.setTagline(resultSet.getString(17));
        movie.setTitle(resultSet.getString(18));
        movie.setVoteAverage(resultSet.getDouble(19));
        movie.setVoteCount(resultSet.getLong(20));
        return movie;
    };

    private final RowMapper<Movie> simpleRowMapper = (resultSet, i) -> {
        Movie movie = new Movie();
        movie.setId(resultSet.getLong(1));
        movie.setTitle(resultSet.getString(2));
        movie.setPopularity(resultSet.getDouble(3));
        return movie;
    };

    public MovieRepository(DataSource source, Gson gson) {
        template = new NamedParameterJdbcTemplate(source);
        this.gson = gson;
    }

    @PostConstruct
    public void init() {
        template.getJdbcTemplate().execute("CREATE TABLE IF NOT EXISTS movies(" +
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
                "revenue INTEGER , " +
                "runtime INTEGER, " +
                "spoken_languages TEXT, " +
                "status TEXT, " +
                "tagline TEXT, " +
                "title TEXT, " +
                "vote_average REAL, " +
                "vote_count INTEGER)");
    }

    public Movie getById(long id) {
       List<Movie> list = template.query("SELECT " +
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
               "vote_count " +
               "FROM movies WHERE id = :id", Map.of("id", id), fullRowMapper);
       return list.get(0);
    }

    public List<Movie> getListOf20(int offset) {
        return template.query(
                "SELECT id, title, popularity FROM movies ORDER BY popularity DESC LIMIT :offset, 20",
                Map.of("offset", offset), simpleRowMapper);
    }

    public List<Movie> getTop20() {
        return template.query(
                "SELECT id, title, popularity FROM movies ORDER BY popularity DESC LIMIT 20",
                simpleRowMapper);
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
                entry("genres", gson.toJson(movie.getGenres())),
                entry("homepage", movie.getHomePage()),
                entry("id", movie.getId()),
                entry("keywords", gson.toJson(movie.getKeywords())),
                entry("original_language", movie.getOriginalLanguage()),
                entry("original_title", movie.getOriginalTitle()),
                entry("overview", movie.getOverview()),
                entry("popularity", movie.getPopularity()),
                entry("production_companies", gson.toJson(movie.getProductionCompanies())),
                entry("production_countries", gson.toJson(movie.getProductionCountries())),
                entry("release_date", movie.getReleaseDate()),
                entry("revenue", movie.getRevenue()),
                entry("runtime", movie.getRuntime()),
                entry("spoken_languages", gson.toJson(movie.getSpokenLanguages())),
                entry("status", movie.getStatus()),
                entry("tagline", movie.getTagline()),
                entry("title", movie.getTitle()),
                entry("vote_average", movie.getVoteAverage()),
                entry("vote_count", movie.getVoteCount())
               );
    }
}
