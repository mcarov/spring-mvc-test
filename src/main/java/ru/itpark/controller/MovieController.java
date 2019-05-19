package ru.itpark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itpark.domain.Genre;
import ru.itpark.domain.Keyword;
import ru.itpark.domain.Movie;
import ru.itpark.domain.ProductionCompany;
import ru.itpark.service.MovieService;
import ru.itpark.service.TranslatorService;

import java.io.IOException;
import java.util.*;

import static ru.itpark.Constants.LIST_SIZE;

@Controller
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    private final TranslatorService translatorService;

    private final List<String> navbarItems = new ArrayList<>(Arrays.asList(
            "navbar.top-20", "navbar.movies", "navbar.collections", "navbar.genres",
            "navbar.companies", "navbar.add-movie", "navbar.choose-file", "navbar.import"));
    private final List<String> movieItems = new ArrayList<>(Arrays.asList(
            "movie.title", "movie.tagline",
            "movie.original-title", "movie.original-language", "movie.release-date", "movie.status", "movie.runtime",
            "movie.genres", "movie.companies", "movie.countries", "movie.spoken-languages", "movie.budget",
            "movie.revenue", "movie.homepage", "movie.popularity", "movie.votes", "movie.rating", "movie.overview", "movie.keywords"));
    private final List<String> tableItems = new ArrayList<>(Arrays.asList(
            "table.movie", "table.popularity"));
    private final List<String> deletingItems = new ArrayList<>(Arrays.asList(
            "text.movie", "text.from", "button.yes", "button.no"
    ));

    @GetMapping("/")
    public String getTop20(Model model) {
        model.addAllAttributes(Map.of(
                "translation", translatorService.translate(navbarItems, tableItems),
                "movies", movieService.getMoviesTop20()));
        return "main";
    }

    @GetMapping("/movies/page/{num}")
    public String getList(Model model, @PathVariable int num) {
        int lastNum = (int)Math.ceil(movieService.getMovieRepoSize()*1.0/LIST_SIZE);
        if(num > lastNum) num = lastNum;
        model.addAllAttributes(Map.of(
                "translation", translatorService.translate(navbarItems, tableItems),
                "movies", movieService.getMovies(num),
                "page", num,
                "last", lastNum));
        return "movies";
    }

    @GetMapping("/movies/{id}")
    public String getMovie(Model model, @PathVariable long id) {
        model.addAllAttributes(Map.of(
                "translation", translatorService.translate(navbarItems, movieItems, Arrays.asList("button.edit", "button.remove", "value.time")),
                "movie", movieService.getMovieById(id)));
        return "movie";
    }

    @GetMapping("/collections/page/{num}")
    public String getCollections(Model model, @PathVariable int num) {
        int lastNum = (int)Math.ceil(movieService.getKeywordRepoSize()*1.0/LIST_SIZE);
        if(num > lastNum) num = lastNum;
        model.addAllAttributes(Map.of(
                "translation", translatorService.translate(navbarItems, "table.collection"),
                "collections", movieService.getCollections(num),
                "page", num,
                "last", lastNum));
        return "collections";
    }

    @GetMapping("/collections/{id}")
    public String getListOfCollection(Model model, @PathVariable long id) {
        List<Movie> list = movieService.getMoviesByCollectionId(id);
        Keyword collection = Arrays.stream(list.get(0).getKeywords()).filter(k -> k.getId() == id).findFirst().get();
        model.addAllAttributes(Map.of(
                "translation", translatorService.translate(navbarItems, tableItems),
                "movies", list,
                "collection", collection.getName()));
        return "collection";
    }

    @GetMapping("/genres")
    public String getGenres(Model model) {
        model.addAllAttributes(Map.of(
                "translation", translatorService.translate(navbarItems, "table.genre"),
                "genres", movieService.getGenres()));
        return "genres";
    }

    @GetMapping("/genres/{id}")
    public String getTop20OfGenre(Model model, @PathVariable long id) {
        List<Movie> list = movieService.getMoviesByGenreId(id);
        Genre genre = Arrays.stream(list.get(0).getGenres()).filter(g -> g.getId() == id).findFirst().get();
        model.addAllAttributes(Map.of(
                "translation", translatorService.translate(navbarItems, tableItems),
                "movies", list,
                "genre", genre.getName()));
        return "genre";
    }

    @GetMapping("/companies/page/{num}")
    public String getCompanies(Model model, @PathVariable int num) {
        int lastNum = (int)Math.ceil(movieService.getCompanyRepoSize()*1.0/LIST_SIZE);
        if(num > lastNum) num = lastNum;
        model.addAllAttributes(Map.of(
                "translation", translatorService.translate(navbarItems, "table.company"),
                "companies", movieService.getCompanies(num),
                "page", num,
                "last", lastNum));
        return "companies";
    }

    @GetMapping("/companies/{id}")
    public String getListOfCompany(Model model, @PathVariable long id) {
        List<Movie> list = movieService.getMoviesByCompanyId(id);
        ProductionCompany company = Arrays.stream(list.get(0).getProductionCompanies()).filter(c -> c.getId() == id).findFirst().get();
        model.addAllAttributes(Map.of(
                "translation", translatorService.translate(navbarItems, Arrays.asList("table.movie", "table.release-date")),
                "movies", list,
                "company", company.getName()));
        return "company";
    }

    @GetMapping("/movies/{id}/edit")
    public String createNewMovie(Model model, @PathVariable long id) {
        if(id == 0) {
            model.addAttribute("translation", translatorService.translate(navbarItems, movieItems, Collections.singletonList("button.save")));
        }
        else {
            model.addAllAttributes(Map.of(
                    "translation", translatorService.translate(navbarItems, movieItems, Collections.singletonList("button.save")),
                    "movie", movieService.getMovieById(id)));
        }
        return "edit";
    }

    @GetMapping("/movies/{id}/remove")
    public String removeMovieConfirm(Model model, @PathVariable long id) {
        model.addAllAttributes(Map.of(
                "translation", translatorService.translate(deletingItems),
                "movie", movieService.getMovieById(id)));
        return "remove";
    }

    @PostMapping("/import")
    public String importFromFile(@RequestParam("csv-file") MultipartFile file) throws IOException {
        if(!file.isEmpty()) {
            movieService.updateDatabaseFromFile(file);
        }
        return "redirect:/";
    }

    @PostMapping("/movies/{id}/save")
    public String saveMovie(@PathVariable long id) {
        movieService.saveMovieById(id);
        System.out.println("saved "+id);
        return "redirect:/";
    }

    @PostMapping("/movies/{id}/remove")
    public String removeMovie(@PathVariable long id) {
        movieService.removeMovieById(id);
        return "redirect:/";
    }
}
