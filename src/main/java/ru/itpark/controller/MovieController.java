package ru.itpark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.itpark.domain.Genre;
import ru.itpark.domain.Keyword;
import ru.itpark.domain.Movie;
import ru.itpark.domain.ProductionCompany;
import ru.itpark.service.MovieService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MovieController {
    private final MovieService service;

    @GetMapping("/")
    public String getTop20(Model model) throws IOException {
        service.updateFromFile();
        model.addAttribute("movies", service.getTop20());
        return "main";
    }

    @GetMapping("/movies/page/{num}")
    public String getListOf50(Model model, @PathVariable int num) {
        int lastNum = (int)Math.ceil(service.getRepositorySize()/50.0);
        model.addAllAttributes(Map.of("movies", service.getListOf50(num), "page", num, "last", lastNum));
        return "movies";
    }

    @GetMapping("/movies/{id}")
    public String getMovie(Model model, @PathVariable long id) {
        model.addAttribute("movie", service.getById(id));
        return "movie";
    }

    @GetMapping("/genres")
    public String getGenres(Model model) {
        model.addAttribute("genres", service.getGenres());
        return "genres";
    }

    @GetMapping("/genres/{id}")
    public String getTop20OfGenre(Model model, @PathVariable long id) {
        List<Movie> list = service.getTop20OfGenre(id);
        Genre genre = Arrays.stream(list.get(0).getGenres()).filter(g -> g.getId() == id).findFirst().get();
        model.addAllAttributes(Map.of("movies", list, "genre", genre.getName()));
        return "genre";
    }

    @GetMapping("/companies/page/{num}")
    public String getCompanies(Model model, @PathVariable int num) {
        List<ProductionCompany> companies = service.getCompanies();
        int lastNum = (int)Math.ceil(companies.size()/50.0);
        model.addAllAttributes(Map.of("companies", companies.subList(50*(num-1), 50*num < companies.size() ? 50*num : companies.size()), "page", num, "last", lastNum));
        return "companies";
    }

    @GetMapping("/companies/{id}")
    public String getNewestOfCompany(Model model, @PathVariable long id) {
        List<Movie> list = service.getMoviesOfCompany(id);
        ProductionCompany company = Arrays.stream(list.get(0).getProductionCompanies()).filter(c -> c.getId() == id).findFirst().get();
        model.addAllAttributes(Map.of("movies", list, "company", company.getName()));
        return "company";
    }

    @GetMapping("/collections/page/{num}")
    public String getCollections(Model model, @PathVariable int num) {
        List<Keyword> collections = service.getCollections();
        int lastNum = (int)Math.ceil(collections.size()/50.0);
        model.addAllAttributes(Map.of("collections", collections.subList(50*(num-1), 50*num < collections.size() ? 50*num : collections.size()), "page", num, "last", lastNum));
        return "collections";
    }

    @GetMapping("/collections/{id}")
    public String getCollection(Model model, @PathVariable long id) {
        List<Movie> list = service.getMoviesOfCollection(id);
        Keyword collection = Arrays.stream(list.get(0).getKeywords()).filter(k -> k.getId() == id).findFirst().get();
        model.addAllAttributes(Map.of("movies", list, "collection", collection.getName()));
        return "collection";
    }
}
