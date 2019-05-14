package ru.itpark.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class TranslatorService {
    private final MessageSource messageSource;

    public TranslatorService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String translate(String message) {
        return messageSource.getMessage(message,null, Locale.getDefault());
    }

    public List<String> translate(List<String> messages) {
        return messages.stream().
                map(s -> messageSource.getMessage(s,null, Locale.getDefault())).
                collect(Collectors.toList());
    }
}
