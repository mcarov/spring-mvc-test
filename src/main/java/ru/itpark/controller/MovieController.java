package ru.itpark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itpark.domain.Genre;
import ru.itpark.domain.Movie;
import ru.itpark.domain.ProductionCompany;
import ru.itpark.service.MovieService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
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
    public String getListOf20(Model model, @PathVariable int num) {
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

    @GetMapping("/companies")
    public String getCompanies(Model model) {
        model.addAttribute("companies", service.getCompanies());
        return "companies";
    }

    @GetMapping("/companies/{id}")
    public String getNewestOfCompany(Model model, @PathVariable long id) {
        List<Movie> list = service.getNewest20OfCompany(id);
        ProductionCompany company = Arrays.stream(list.get(0).getProductionCompanies()).filter(c -> c.getId() == id).findFirst().get();
        model.addAllAttributes(Map.of("movies", list, "company", company.getName()));
        return "company";
    }
}
