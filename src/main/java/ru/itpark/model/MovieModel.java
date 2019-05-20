package ru.itpark.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itpark.domain.Movie;

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

        return movie;
    }
}
