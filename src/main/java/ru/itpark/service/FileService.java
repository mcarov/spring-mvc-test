package ru.itpark.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itpark.domain.*;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final Gson gson;
    private final String uploadPath = System.getenv("UPLOAD_PATH");

    List<Movie> importFromCsvFile(MultipartFile file) throws IOException {
        String csvFileId = UUID.randomUUID().toString();
        file.transferTo(Paths.get(uploadPath).resolve(csvFileId));

        try(Reader reader = Files.newBufferedReader(Paths.get(uploadPath).resolve(csvFileId));
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.
                    withFirstRecordAsHeader().
                    withIgnoreHeaderCase().
                    withTrim())) {
            List<Movie> list = new ArrayList<>();
            for(CSVRecord record : parser) {
                list.add(getMovie(record));
            }
            return list;
        }
    }

    private Movie getMovie(CSVRecord record) {
        Movie movie = new Movie();

        movie.setBudget(NumberUtils.toLong(record.get("budget")));
        movie.setGenres(
                gson.fromJson(record.get("genres"), Genre[].class));
        movie.setHomepage(record.get("homepage"));
        movie.setId(NumberUtils.toLong(record.get("id")));
        movie.setKeywords(
                gson.fromJson(record.get("keywords"), Keyword[].class));
        movie.setOriginalLanguage(record.get("original_language"));
        movie.setOriginalTitle(record.get("original_title"));
        movie.setOverview(record.get("overview"));
        movie.setPopularity(Double.parseDouble(record.get("popularity")));
        movie.setProductionCompanies(
                gson.fromJson(record.get("production_companies"), ProductionCompany[].class));
        movie.setProductionCountries(
                gson.fromJson(record.get("production_countries"), ProductionCountry[].class));
        try {
            movie.setReleaseDate(DateUtils.parseDate(record.get("release_date"), "yyyy-MM-dd"));
        }
        catch(ParseException e) {
            movie.setReleaseDate(new Date(0));
        }
        movie.setRevenue(NumberUtils.toLong(record.get("revenue")));
        movie.setRuntime((int)NumberUtils.toDouble(record.get("runtime")));
        movie.setSpokenLanguages(
                gson.fromJson(record.get("spoken_languages"), SpokenLanguage[].class));
        movie.setStatus(record.get("status"));
        movie.setTagline(record.get("tagline"));
        movie.setTitle(record.get("title"));
        movie.setVoteAverage(NumberUtils.toDouble(record.get("vote_average")));
        movie.setVoteCount(NumberUtils.toLong(record.get("vote_count")));

        return movie;
    }
}
