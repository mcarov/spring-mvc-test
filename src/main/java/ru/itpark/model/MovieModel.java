package ru.itpark.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import ru.itpark.domain.*;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class MovieModel {
    private long id;
    private String budget;
    private String genres;
    private String homepage;
    private String keywords;
    private String original_language;
    private String original_title;
    private String overview;
    private String popularity;
    private String companies;
    private String countries;
    private String date;
    private String revenue;
    private String runtime;
    private String languages;
    private String status;
    private String tagline;
    private String title;
    private String rating;
    private String votes;

    public Movie getMovie() {
        Movie movie = new Movie();

        movie.setId(id);
        movie.setBudget(NumberUtils.toLong(budget));
        Genre[] genres = getStringStream(this.genres).
                map(s -> new Genre(0, s)).toArray(Genre[]::new);
        movie.setGenres(genres);
        movie.setHomepage(homepage);
        Keyword[] keywords = getStringStream(this.keywords).
                map(s -> new Keyword(0, s)).toArray(Keyword[]::new);
        movie.setKeywords(keywords);
        movie.setOriginalLanguage(original_language);
        movie.setOriginalTitle(original_title);
        movie.setOverview(overview);
        movie.setPopularity(NumberUtils.toDouble(popularity));
        ProductionCompany[] companies = getStringStream(this.companies).
                map(s -> new ProductionCompany(s,0)).toArray(ProductionCompany[]::new);
        movie.setProductionCompanies(companies);
        ProductionCountry[] countries = getStringStream(this.countries).
                map(s -> new ProductionCountry("", s)).toArray(ProductionCountry[]::new);
        movie.setProductionCountries(countries);
        try {
            movie.setReleaseDate(DateUtils.parseDate(date, "yyyy-MM-dd"));
        }
        catch(ParseException e) {
            movie.setReleaseDate(new Date(0));
        }
        movie.setRevenue(NumberUtils.toLong(revenue));
        movie.setRuntime(NumberUtils.toInt(runtime));
        SpokenLanguage[] languages = getStringStream(this.languages).
                map(s -> new SpokenLanguage("", s)).toArray(SpokenLanguage[]::new);
        movie.setSpokenLanguages(languages);
        movie.setStatus(status);
        movie.setTagline(tagline);
        movie.setTitle(title);
        movie.setVoteAverage(NumberUtils.toDouble(rating));
        movie.setVoteCount(NumberUtils.toLong(votes));

        return movie;
    }

    private Stream<String> getStringStream(String line) {
        String regex = "[,.;:]";
        return Arrays.stream(line.split(regex)).map(String::trim).filter(s -> !s.equals(""));
    }
}
