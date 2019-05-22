package ru.itpark.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itpark.domain.Keyword;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT)");
    }

    public long size() {
        Optional<Long> size = Optional.ofNullable(template.getJdbcTemplate().queryForObject("SELECT COUNT(*) FROM keywords", Long.class));
        return size.orElse(0L);
    }

    public List<Keyword> getKeywords(int offset) {
        return template.query("SELECT id, name FROM keywords ORDER BY id LIMIT :offset, :limit",
                Map.of("offset", offset, "limit", LIST_SIZE), rowMapper);
    }

    public Keyword getKeywordById(long id) {
        return template.queryForObject("SELECT id, name FROM keywords WHERE id = :id", Map.of("id", id), rowMapper);
    }

    public long getKeywordIdByName(String name) {
        List<Long> keywordList = template.query("SELECT id FROM keywords WHERE name LIKE :name",
                Map.of("name", name), (resultSet, i) -> resultSet.getLong(1));
        return keywordList.isEmpty() ? 0 : keywordList.get(0);
    }

    public void saveKeyword(Keyword keyword) {
        if(keyword.getId() == 0) {
            template.update("INSERT INTO keywords (name) VALUES (:name)",
                    Map.of("name", keyword.getName()));

            Optional<Long> keywordId = Optional.ofNullable(template.getJdbcTemplate().
                    queryForObject("SELECT last_insert_rowid()", Long.class));
            keyword.setId(keywordId.get());
        }
        else {
            template.update("INSERT INTO keywords (id, name) VALUES (:id, :name) " +
                            "ON CONFLICT(id) DO UPDATE SET name = :name WHERE id = :id",
                    Map.of("id", keyword.getId(), "name", keyword.getName()));
        }
    }

    public void removeKeywordById(long id) {
        template.update("DELETE FROM keywords WHERE id = :id", Map.of("id", id));
    }
}
