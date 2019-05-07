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
    public String getTop20(Model model) {
        model.addAttribute("top20", service.getTop20());
        return "top20";
    }
}
