package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itpark.domain.ProductionCountry;
import ru.itpark.repository.CountryRepository;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;

    public String getCountryIsoCodeByName(String name) {
        return countryRepository.getCountryIsoCodeByName(name);
    }

    public ProductionCountry getCountryByIsoCode(String isoCode) {
        return countryRepository.getCountryByIsoCode(isoCode);
    }

    public void saveCountry(ProductionCountry country) {
        countryRepository.saveCountry(country);
    }

    public void removeCountryByIsoCode(String isoCode) {
        countryRepository.removeCountryByIsoCode(isoCode);
    }
}
