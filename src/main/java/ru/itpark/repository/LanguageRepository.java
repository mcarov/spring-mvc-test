package ru.itpark.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itpark.domain.SpokenLanguage;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
public class LanguageRepository {
    private final NamedParameterJdbcTemplate template;

    private final RowMapper<SpokenLanguage> rowMapper = (resultSet, i) -> {
        SpokenLanguage spokenLanguage = new SpokenLanguage();
        spokenLanguage.setIso_639_1(resultSet.getString(1));
        spokenLanguage.setName(resultSet.getString(2));
        return spokenLanguage;
    };

    public LanguageRepository(DataSource source) {
        this.template = new NamedParameterJdbcTemplate(source);
    }

    @PostConstruct
    public void init() {
        template.getJdbcTemplate().execute("CREATE TABLE IF NOT EXISTS languages(" +
                "iso_639_1 TEXT," +
                "name TEXT)");
    }

    public SpokenLanguage getLanguageByIsoCode(String isoCode) {
        List<SpokenLanguage> list = template.query("SELECT iso_639_1, name FROM languages WHERE iso_639_1 LIKE :isoCode", Map.of("isoCode", isoCode), rowMapper);
        return list.get(0);
    }

    public void save(SpokenLanguage spokenLanguage) {
        template.update("INSERT INTO languages (iso_639_1, name) VALUES (:isoCode, :name)",
                Map.of("isoCode", spokenLanguage.getIso_639_1(), "name", spokenLanguage.getName()));
    }
}
