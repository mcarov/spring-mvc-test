package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TranslatorService {
    private final MessageSource messageSource;

    public List<String> translate(List<String> list, String item) {
        return Stream.concat(list.stream(), Stream.of(item)).
                map(s -> messageSource.getMessage(s, null, Locale.getDefault())).
                collect(Collectors.toList());
    }

    public List<String> translate(List<String> list1, List<String> list2) {
        return Stream.concat(list1.stream(), list2.stream()).
                map(s -> messageSource.getMessage(s, null, Locale.getDefault())).
                collect(Collectors.toList());
    }
}
