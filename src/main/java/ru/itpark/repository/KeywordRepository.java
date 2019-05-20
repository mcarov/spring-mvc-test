package ru.itpark.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itpark.domain.Keyword;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

import static ru.itpark.Constants.LIST_SIZE;

@Repository
public class KeywordRepository {
    private final NamedParameterJdbcTemplate template;

    private final RowMapper<Keyword> rowMapper = (resultSet, i) -> {
        Keyword keyword = new Keyword();
        keyword.setId(resultSet.getLong(1));
        keyword.setName(resultSet.getString(2));
        return keyword;
    };

    public KeywordRepository(DataSource source) {
        this.template = new NamedParameterJdbcTemplate(source);
    }

    @PostConstruct
    public void init() {
        template.getJdbcTemplate().execute("CREATE TABLE IF NOT EXISTS keywords(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT)");
    }

    public int size() {
        try {
            return template.getJdbcTemplate().queryForObject("SELECT COUNT(*) FROM keywords", Integer.class);
        }
        catch(NullPointerException e) {
            return 0;
        }
    }

    public List<Keyword> getKeywords(int offset) {
        return template.query("SELECT id, name FROM keywords ORDER BY id LIMIT :offset, :limit",
                Map.of("offset", offset, "limit", LIST_SIZE), rowMapper);
    }

    public Keyword getKeywordById(long id) {
        List<Keyword> list = template.query("SELECT id, name FROM keywords WHERE id = :id", Map.of("id", id), rowMapper);
        return list.get(0);
    }

    public void save(Keyword keyword) {
        if(keyword.getId() == 0) {
            template.update("INSERT INTO keywords (id, name) VALUES (:id, :name)",
                    Map.of("id", keyword.getId(), "name", keyword.getName()));
        }
        else {
            template.update("INSERT INTO keywords (id, name) VALUES (:id, :name) " +
                            "ON CONFLICT(id) DO UPDATE SET name = :name WHERE id = :id",
                    Map.of("id", keyword.getId(), "name", keyword.getName()));
        }
    }
}
