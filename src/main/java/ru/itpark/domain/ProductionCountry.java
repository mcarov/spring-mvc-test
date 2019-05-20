package ru.itpark.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductionCountry {
    private String iso_3166_1;
    private String name;
}
