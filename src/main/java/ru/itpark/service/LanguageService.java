package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itpark.domain.SpokenLanguage;
import ru.itpark.repository.LanguageRepository;

@Service
@RequiredArgsConstructor
public class LanguageService {
    private final LanguageRepository languageRepository;

    public SpokenLanguage getLanguageByIsoCode(String isoCode) {
        return languageRepository.getLanguageByIsoCode(isoCode);
    }

    public String getLanguageIsoCodeByName(String name) {
        return languageRepository.getLanguageIsoCodeByName(name);
    }

    public void saveLanguage(SpokenLanguage language) {
        languageRepository.saveLanguage(language);
    }

    public void removeLanguageByIsoCode(String isoCode) {
        languageRepository.removeLanguageByIsoCode(isoCode);
    }
}
