package ru.itpark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itpark.service.MovieService;

import java.io.IOException;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService service;

    @GetMapping(value = "/")
    public String getTop20(Model model) throws IOException {
        service.updateFromFile();
        model.addAttribute("top20", service.getTop20());
        return "top20";
    }
}
