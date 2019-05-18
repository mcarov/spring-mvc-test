package ru.itpark.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itpark.domain.*;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static ru.itpark.Constants.UPLOAD_PATH;

@Service
@RequiredArgsConstructor
public class FileService {
    private final Gson gson;

    List<Movie> importFromCsvFile(MultipartFile file) throws IOException {
        String csvFileId = UUID.randomUUID().toString();
        file.transferTo(Paths.get(UPLOAD_PATH).resolve(csvFileId));

        try(Reader reader = Files.newBufferedReader(Paths.get(UPLOAD_PATH).resolve(csvFileId));
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
        movie.setBudget(Long.parseLong(record.get("budget")));
        movie.setGenres(
                gson.fromJson(record.get("genres"), Genre[].class));
        movie.setHomePage(record.get("homepage"));
        movie.setId(Long.parseLong(record.get("id")));
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String releaseDate = record.get("release_date");
            if(StringUtils.isNotBlank(releaseDate))
                movie.setReleaseDate(dateFormat.parse(record.get("release_date")));
            else
                movie.setReleaseDate(new Date(0));
        }
        catch(ParseException e) {
            e.printStackTrace();
        }
        movie.setRevenue(Long.parseLong(record.get("revenue")));
        String runtime = record.get("runtime");
        if(StringUtils.isNumeric(runtime))
            movie.setRuntime((int)Double.parseDouble(runtime));
        movie.setSpokenLanguages(
                gson.fromJson(record.get("spoken_languages"), SpokenLanguage[].class));
        movie.setStatus(record.get("status"));
        movie.setTagline(record.get("tagline"));
        movie.setTitle(record.get("title"));
        movie.setVoteAverage(Double.parseDouble(record.get("vote_average")));
        movie.setVoteCount(Long.parseLong(record.get("vote_count")));
        return movie;
    }
}
