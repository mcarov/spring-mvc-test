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
                "iso_3166_1 TEXT," +
                "name TEXT)");
    }

    public ProductionCountry getCountryByIsoCode(String isoCode) {
        List<ProductionCountry> list = template.query("SELECT iso_3166_1, name FROM countries WHERE iso_3166_1 LIKE :isoCode", Map.of("isoCode", isoCode), rowMapper);
        return list.get(0);
    }

    public void save(ProductionCountry productionCountry) {
        template.update("INSERT INTO countries (iso_3166_1, name) VALUES (:isoCode, :name)",
                Map.of("isoCode", productionCountry.getIso_3166_1(), "name", productionCountry.getName()));
    }
}
