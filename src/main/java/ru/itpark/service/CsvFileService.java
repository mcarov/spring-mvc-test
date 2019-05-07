package ru.itpark.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.itpark.domain.Movie;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvFileService {

    List<Movie> importFromCsvFile(String path) throws IOException {
        try(Reader reader = Files.newBufferedReader(Paths.get(path).resolve("tmdb_5000_movies.csv"));
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.
                    withFirstRecordAsHeader().
                    withIgnoreHeaderCase().
                    withTrim())) {
            List<Movie> movies = new ArrayList<>();
            for(CSVRecord record : parser) {
                movies.add(getMovie(record));
            }
            return movies;
        }
    }

    private Movie getMovie(CSVRecord record) {
        Movie movie = new Movie();
        movie.setBudget(Long.parseLong(record.get("budget")));
        movie.setGenres(record.get("genres"));
        movie.setHomePage(record.get("homepage"));
        movie.setId(Long.parseLong(record.get("id")));
        movie.setKeywords(record.get("keywords"));
        movie.setOriginalLanguage(record.get("original_language"));
        movie.setOriginalTitle(record.get("original_title"));
        movie.setOverview(record.get("overview"));
        movie.setPopularity(Double.parseDouble(record.get("popularity")));
        movie.setProductionCompanies(record.get("production_companies"));
        movie.setProductionCountries(record.get("production_countries"));
        movie.setReleaseDate(record.get("release_date"));
        movie.setRevenue(Long.parseLong(record.get("revenue")));
        String runtime = record.get("runtime");
        if(StringUtils.isNumeric(runtime))
            movie.setRuntime((int)Double.parseDouble(runtime));
        movie.setSpokenLanguages(record.get("spoken_languages"));
        movie.setStatus(record.get("status"));
        movie.setTagline(record.get("tagline"));
        movie.setTitle(record.get("title"));
        movie.setVoteAverage(Double.parseDouble(record.get("vote_average")));
        movie.setVoteCount(Long.parseLong(record.get("vote_count")));
        return movie;
    }
}
