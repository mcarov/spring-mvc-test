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

    public List<String> translate(List<String> items, String item) {
        return Stream.concat(items.stream(), Stream.of(item)).
                map(s -> messageSource.getMessage(s, null, Locale.getDefault())).
                collect(Collectors.toList());
    }

    @SafeVarargs
    public final List<String> translate(List<String>... items) {
        return Stream.of(items).flatMap(List::stream).
                map(s -> messageSource.getMessage(s, null, Locale.getDefault())).
                collect(Collectors.toList());
    }
}
