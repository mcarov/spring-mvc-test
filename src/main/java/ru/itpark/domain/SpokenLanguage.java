package ru.itpark.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SpokenLanguage {
    private String iso_639_1;
    private String name;
}
