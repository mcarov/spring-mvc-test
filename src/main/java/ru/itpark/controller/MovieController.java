package ru.itpark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.itpark.Constants;
import ru.itpark.domain.Genre;
import ru.itpark.domain.Keyword;
import ru.itpark.domain.Movie;
import ru.itpark.domain.ProductionCompany;
import ru.itpark.service.MovieService;
import ru.itpark.service.TranslatorService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MovieController implements Constants {
    private final MovieService movieService;
    private final TranslatorService translatorService;
    private List<String> navbarItems = Arrays.asList("navbar.top-20", "navbar.movies", "navbar.collections", "navbar.genres", "navbar.companies");

    @GetMapping("/")
    public String getTop20(Model model) {
        model.addAllAttributes(Map.of(
                "movies", movieService.getTop20(),
                "translation", translatorService.translate(navbarItems)));
        return "main";
    }

    @GetMapping("/movies/page/{num}")
    public String getListOf50(Model model, @PathVariable int num) {
        int lastNum = (int)Math.ceil(movieService.getRepositorySize()*1.0/LIST_SIZE);
        if(num > lastNum) num = lastNum;
        model.addAllAttributes(Map.of(
                "movies", movieService.getList(num),
                "translation", translatorService.translate(navbarItems),
                "page", num,
                "last", lastNum));
        return "movies";
    }

    @GetMapping("/movies/{id}")
    public String getMovie(Model model, @PathVariable long id) {
        model.addAllAttributes(Map.of(
                "movie", movieService.getById(id),
                "translation", translatorService.translate(navbarItems)));
        return "movie";
    }

    @GetMapping("/genres")
    public String getGenres(Model model) {
        model.addAllAttributes(Map.of(
                "genres", movieService.getGenres(),
                "translation", translatorService.translate(navbarItems)));
        return "genres";
    }

    @GetMapping("/genres/{id}")
    public String getTop20OfGenre(Model model, @PathVariable long id) {
        List<Movie> list = movieService.getTop20OfGenre(id);
        Genre genre = Arrays.stream(list.get(0).getGenres()).filter(g -> g.getId() == id).findFirst().get();
        model.addAllAttributes(Map.of(
                "movies", list,
                "genre", genre.getName(),
                "translation", translatorService.translate(navbarItems)));
        return "genre";
    }

    @GetMapping("/companies/page/{num}")
    public String getCompanies(Model model, @PathVariable int num) {
        List<ProductionCompany> companies = movieService.getCompanies();
        int lastNum = (int)Math.ceil(companies.size()*1.0/LIST_SIZE);
        if(num > lastNum) num = lastNum;
        model.addAllAttributes(Map.of(
                "companies", companies.subList(LIST_SIZE*(num-1), LIST_SIZE*num < companies.size() ? LIST_SIZE*num : companies.size()),
                "translation", translatorService.translate(navbarItems),
                "page", num, "last", lastNum));
        return "companies";
    }

    @GetMapping("/companies/{id}")
    public String getNewestOfCompany(Model model, @PathVariable long id) {
        List<Movie> list = movieService.getMoviesOfCompany(id);
        ProductionCompany company = Arrays.stream(list.get(0).getProductionCompanies()).filter(c -> c.getId() == id).findFirst().get();
        model.addAllAttributes(Map.of(
                "movies", list,
                "company", company.getName(),
                "translation", translatorService.translate(navbarItems)));
        return "company";
    }

    @GetMapping("/collections/page/{num}")
    public String getCollections(Model model, @PathVariable int num) {
        List<Keyword> collections = movieService.getCollections();
        int lastNum = (int)Math.ceil(collections.size()*1.0/LIST_SIZE);
        if(num > lastNum) num = lastNum;
        model.addAllAttributes(Map.of(
                "collections", collections.subList(LIST_SIZE*(num-1), LIST_SIZE*num < collections.size() ? LIST_SIZE*num : collections.size()),
                "translation", translatorService.translate(navbarItems),
                "page", num, "last", lastNum));
        return "collections";
    }

    @GetMapping("/collections/{id}")
    public String getCollection(Model model, @PathVariable long id) {
        List<Movie> list = movieService.getMoviesOfCollection(id);
        Keyword collection = Arrays.stream(list.get(0).getKeywords()).filter(k -> k.getId() == id).findFirst().get();
        model.addAllAttributes(Map.of(
                "movies", list,
                "collection", collection.getName(),
                "translation", translatorService.translate(navbarItems)));
        return "collection";
    }

    @PostMapping("/import")
    public String importDatabase(@RequestParam("csv-file") MultipartFile file) throws IOException {
        if(!file.isEmpty()) {
            movieService.updateFromFile(file);
        }
        return "redirect:/";
    }
}
