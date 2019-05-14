package ru.itpark.repository;

import com.google.gson.Gson;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itpark.Constants;
import ru.itpark.domain.*;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
public class MovieRepository implements Constants {
    private final NamedParameterJdbcTemplate template;
    private Gson gson;

    private final RowMapper<Movie> movieFullRowMapper = (resultSet, i) -> {
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
        movie.setReleaseDate(Date.from(Instant.ofEpochSecond(resultSet.getLong(12))));
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

    private final RowMapper<Movie> movieSimpleRowMapper = (resultSet, i) -> {
        Movie movie = new Movie();
        movie.setId(resultSet.getLong(1));
        movie.setTitle(resultSet.getString(2));
        movie.setPopularity(resultSet.getDouble(3));
        movie.setReleaseDate(Date.from(Instant.ofEpochSecond(resultSet.getLong(4))));
        movie.setGenres(
                gson.fromJson(resultSet.getString(5), Genre[].class));
        movie.setProductionCompanies(
                gson.fromJson(resultSet.getString(6), ProductionCompany[].class));
        movie.setKeywords(
                gson.fromJson(resultSet.getString(7), Keyword[].class));
        return movie;
    };

    private final RowMapper<Genre[]> genreRowMapper = (resultSet, i) ->
            gson.fromJson(resultSet.getString(1), Genre[].class);

    private final RowMapper<ProductionCompany[]> companyRowMapper = (resultSet, i) ->
            gson.fromJson(resultSet.getString(1), ProductionCompany[].class);

    private final RowMapper<Keyword[]> collectionRowMapper = (resultSet, i) ->
            gson.fromJson(resultSet.getString(1), Keyword[].class);

    public MovieRepository(DataSource source, Gson gson) {
        this.template = new NamedParameterJdbcTemplate(source);
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
                "release_date INTEGER, " +
                "revenue INTEGER , " +
                "runtime INTEGER, " +
                "spoken_languages TEXT, " +
                "status TEXT, " +
                "tagline TEXT, " +
                "title TEXT, " +
                "vote_average REAL, " +
                "vote_count INTEGER)");
    }

    public int size() {
        return template.getJdbcTemplate().queryForObject("SELECT COUNT(*) FROM movies", Integer.class);
    }

    public Movie getById(long id) {
       List<Movie> list = template.query("SELECT budget, " +
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
               "vote_count FROM movies WHERE id = :id", Map.of("id", id), movieFullRowMapper);
       return list.get(0);
    }

    public List<Movie> getTop20() {
        return template.query(
                "SELECT id, " +
                        "title, " +
                        "popularity, " +
                        "release_date, " +
                        "genres, " +
                        "production_companies, " +
                        "keywords FROM movies ORDER BY popularity DESC LIMIT 20",
                movieSimpleRowMapper);
    }

    public List<Movie> getList(int offset) {
        return template.query(
                "SELECT id, " +
                        "title, " +
                        "popularity, " +
                        "release_date, " +
                        "genres, " +
                        "production_companies, " +
                        "keywords FROM movies ORDER BY popularity DESC LIMIT :offset, :limit",
                Map.of("offset", offset, "limit", LIST_SIZE), movieSimpleRowMapper);
    }

    public List<Movie> getTop20OfGenre(long id) {
        return template.query(
                "SELECT id, " +
                        "title, " +
                        "popularity, " +
                        "release_date, " +
                        "genres, " +
                        "production_companies, " +
                        "keywords FROM movies WHERE genres LIKE :pattern ORDER BY popularity DESC LIMIT 20",
                Map.of("pattern", String.join("", "%\"id\":", Long.toString(id), ",%")), movieSimpleRowMapper);
    }

    public List<Movie> getListByCompany(long id) {
        return template.query(
                "SELECT id, " +
                        "title, " +
                        "popularity, " +
                        "release_date, " +
                        "genres, " +
                        "production_companies, " +
                        "keywords FROM movies WHERE production_companies LIKE :pattern ORDER BY release_date DESC",
                Map.of("pattern", String.join("", "%\"id\":", Long.toString(id), "}%")), movieSimpleRowMapper);
    }

    public List<Movie> getListByCollection(long id) {
        return template.query(
                "SELECT id, " +
                        "title, " +
                        "popularity, " +
                        "release_date, " +
                        "genres, " +
                        "production_companies, " +
                        "keywords FROM movies WHERE keywords LIKE :pattern ORDER BY popularity DESC",
                Map.of("pattern", String.join("", "%\"id\":", Long.toString(id), ",%")), movieSimpleRowMapper);
    }

    public List<Genre[]> getGenres() {
        return template.query("SELECT genres FROM movies", genreRowMapper);
    }

    public List<ProductionCompany[]> getCompanies() {
        return template.query("SELECT production_companies FROM movies", companyRowMapper);
    }

    public List<Keyword[]> getCollections() {
        return template.query("SELECT keywords FROM movies", collectionRowMapper);
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
        return Map.ofEntries(
                entry("budget", movie.getBudget()),
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
                entry("release_date",movie.getReleaseDate().getTime()/1000),
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
