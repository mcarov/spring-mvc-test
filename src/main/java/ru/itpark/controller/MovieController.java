package ru.itpark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itpark.service.MovieService;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService service;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("top20", service.getAll());
        return "top20";
    }
}
