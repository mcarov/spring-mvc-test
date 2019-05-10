package ru.itpark.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    private long budget;
    private Genre[] genres;
    private String homePage;
    private long id;
    private Keyword[] keywords;
    private String originalLanguage;
    private String originalTitle;
    private String overview;
    private double popularity;
    private ProductionCompany[] productionCompanies;
    private ProductionCountry[] productionCountries;
    private String releaseDate;
    private long revenue;
    private int runtime;
    private SpokenLanguage[] spokenLanguages;
    private String status;
    private String tagline;
    private String title;
    private double voteAverage;
    private long voteCount;
}
