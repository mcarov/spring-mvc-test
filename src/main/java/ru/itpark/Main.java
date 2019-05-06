package ru.itpark;

import ru.itpark.service.CsvFileService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        CsvFileService service = new CsvFileService();
        service.importFromCsvFile("C:/Users/KO-01/Desktop/Spring");
    }
}
