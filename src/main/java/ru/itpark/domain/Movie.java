package ru.itpark.domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")
public class Movie {
    private long budget;
    private Genre[] genres;
    private String homepage;
    private long id;
    private Keyword[] keywords;
    private String originalLanguage;
    private String originalTitle;
    private String overview;
    private double popularity;
    private ProductionCompany[] productionCompanies;
    private ProductionCountry[] productionCountries;
    private Date releaseDate;
    private long revenue;
    private int runtime;
    private SpokenLanguage[] spokenLanguages;
    private String status;
    private String tagline;
    private String title;
    private double voteAverage;
    private long voteCount;
}
