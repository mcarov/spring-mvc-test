package ru.itpark.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itpark.domain.ProductionCountry;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
public class CountryRepository {
    private final NamedParameterJdbcTemplate template;

    private final RowMapper<ProductionCountry> rowMapper = (resultSet, i) -> {
        ProductionCountry productionCountry = new ProductionCountry();
        productionCountry.setIso_3166_1(resultSet.getString(1));
        productionCountry.setName(resultSet.getString(2));
        return productionCountry;
    };

    public CountryRepository(DataSource source) {
        this.template = new NamedParameterJdbcTemplate(source);
    }

    @PostConstruct
    public void init() {
        template.getJdbcTemplate().execute("CREATE TABLE IF NOT EXISTS countries(" +
                "iso_3166_1 TEXT PRIMARY KEY, " +
                "name TEXT)");
    }

    public ProductionCountry getCountryByIsoCode(String isoCode) {
        return template.queryForObject("SELECT iso_3166_1, name FROM countries WHERE iso_3166_1 LIKE :isoCode", Map.of("isoCode", isoCode), rowMapper);
    }

    public String getCountryIsoCodeByName(String name) {
        List<String> countryList = template.query("SELECT iso_3166_1 FROM countries WHERE name LIKE :name",
                Map.of("name", name), (resultSet, i) -> resultSet.getString(1));
        return countryList.isEmpty() ? "" : countryList.get(0);
    }

    public void saveCountry(ProductionCountry country) {
        if(country.getIso_3166_1().equals("")) {
            String isoCode = "zz";
            template.update("INSERT INTO countries (iso_3166_1, name) VALUES (:isoCode, :name) " +
                            "ON CONFLICT(iso_3166_1) DO UPDATE SET name = :name WHERE iso_3166_1 = :isoCode",
                    Map.of("isoCode", isoCode, "name", country.getName()));

            country.setIso_3166_1(isoCode);
        }
        else {
            template.update("INSERT INTO countries (iso_3166_1, name) VALUES (:isoCode, :name) " +
                            "ON CONFLICT(iso_3166_1) DO UPDATE SET name = :name WHERE iso_3166_1 = :isoCode",
                    Map.of("isoCode", country.getIso_3166_1(), "name", country.getName()));
        }
    }

    public void removeCountryByIsoCode(String isoCode) {
        template.update("DELETE FROM countries WHERE iso_3166_1 LIKE :isoCode", Map.of("isoCode", isoCode));
    }
}
