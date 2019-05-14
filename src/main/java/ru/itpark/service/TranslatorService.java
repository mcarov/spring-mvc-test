package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TranslatorService {
    private final MessageSource messageSource;

    public String translate(String message) {
        return messageSource.getMessage(message,null, LocaleContextHolder.getLocale());
    }

    public List<String> translate(List<String> messages) {
        return messages.stream().
                map(s -> messageSource.getMessage(s,null, LocaleContextHolder.getLocale())).
                collect(Collectors.toList());
    }
}
