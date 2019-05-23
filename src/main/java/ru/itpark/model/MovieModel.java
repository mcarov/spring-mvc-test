package ru.itpark.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import ru.itpark.domain.*;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
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

    public MovieBuilder getBuilder() {
        return new MovieBuilder();
    }

    public class MovieBuilder {
        private Movie movie;

        private MovieBuilder() {
            movie = new Movie();
        }

        public Movie build() {
            movie.setId(id);
            movie.setBudget(NumberUtils.toLong(budget));
            Genre[] genreArray = getStringStream(genres).
                    map(s -> new Genre(0, s)).toArray(Genre[]::new);
            movie.setGenres(genreArray);
            movie.setHomepage(homepage);
            Keyword[] keywordArray = getStringStream(keywords).
                    map(s -> new Keyword(0, s)).toArray(Keyword[]::new);
            movie.setKeywords(keywordArray);
            movie.setOriginalLanguage(original_language);
            movie.setOriginalTitle(original_title);
            movie.setOverview(overview);
            movie.setPopularity(NumberUtils.toDouble(popularity));
            ProductionCompany[] companyArray = getStringStream(companies).
                    map(s -> new ProductionCompany(s,0)).toArray(ProductionCompany[]::new);
            movie.setProductionCompanies(companyArray);
            ProductionCountry[] countryArray = getStringStream(countries).
                    map(s -> new ProductionCountry(getCountryIsoCodeByName(s), s)).toArray(ProductionCountry[]::new);
            movie.setProductionCountries(countryArray);
            try {
                movie.setReleaseDate(DateUtils.parseDate(date, "yyyy-MM-dd"));
            }
            catch(ParseException e) {
                movie.setReleaseDate(new Date(0));
            }
            movie.setRevenue(NumberUtils.toLong(revenue));
            movie.setRuntime(NumberUtils.toInt(runtime));
            SpokenLanguage[] languageArray = getStringStream(languages).
                    map(s -> new SpokenLanguage(getLanguageIsoCodeByName(s), s)).toArray(SpokenLanguage[]::new);
            movie.setSpokenLanguages(languageArray);
            movie.setStatus(status);
            movie.setTagline(tagline);
            movie.setTitle(title);
            movie.setVoteAverage(NumberUtils.toDouble(rating));
            movie.setVoteCount(NumberUtils.toLong(votes));

            return movie;
        }
    }

    private Stream<String> getStringStream(String line) {
        return Arrays.stream(line.split("[,;]")).filter(StringUtils::isNotBlank).map(String::trim);
    }

    private String getCountryIsoCodeByName(String name) {
        Optional<Locale> locale = Arrays.stream(Locale.getAvailableLocales()).filter(l -> StringUtils.isNotBlank(l.getDisplayCountry(l))).
                filter(l -> StringUtils.containsIgnoreCase(l.getDisplayCountry(l), name) |
                        StringUtils.containsIgnoreCase(name, l.getDisplayCountry(l))).findFirst();
        return locale.isPresent() ? locale.get().getCountry() : "";
    }

    private String getLanguageIsoCodeByName(String name) {
        Optional<Locale> locale = Arrays.stream(Locale.getAvailableLocales()).filter(l -> StringUtils.isNotBlank(l.getDisplayLanguage(l))).
                filter(l -> StringUtils.containsIgnoreCase(l.getDisplayLanguage(l), name) |
                        StringUtils.containsIgnoreCase(name, l.getDisplayLanguage(l))).findFirst();
        return locale.isPresent() ? locale.get().getLanguage() : "";
    }
}
