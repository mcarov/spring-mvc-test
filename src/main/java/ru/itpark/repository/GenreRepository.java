package ru.itpark.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itpark.domain.Genre;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ru.itpark.Constants.LIST_SIZE;

@Repository
public class GenreRepository {
    private final NamedParameterJdbcTemplate template;

    private final RowMapper<Genre> rowMapper = (resultSet, i) -> {
        Genre genre = new Genre();
        genre.setId(resultSet.getLong(1));
        genre.setName(resultSet.getString(2));
        return genre;
    };

    public GenreRepository(DataSource source) {
        this.template = new NamedParameterJdbcTemplate(source);
    }

    @PostConstruct
    public void init() {
        template.getJdbcTemplate().execute("CREATE TABLE IF NOT EXISTS genres(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT)");
    }

    public List<Genre> getGenres(int offset) {
        return template.query("SELECT id, name FROM genres ORDER BY id LIMIT :offset, :limit",
                Map.of("offset", offset, "limit", LIST_SIZE), rowMapper);
    }

    public Genre getGenreById(long id) {
        return template.queryForObject("SELECT id, name FROM genres WHERE id = :id", Map.of("id", id), rowMapper);
    }

    public long getGenreIdByName(String name) {
        List<Long> genreList = template.query("SELECT id FROM genres WHERE name LIKE :name",
                Map.of("name", name), (resultSet, i) -> resultSet.getLong(1));
        return genreList.isEmpty() ? 0 : genreList.get(0);
    }

    public void saveGenre(Genre genre) {
        if(genre.getId() == 0) {
            template.update("INSERT INTO genres (name) VALUES (:name)",
                    Map.of("name", genre.getName()));

            Optional<Long> genreId = Optional.ofNullable(template.getJdbcTemplate().
                    queryForObject("SELECT last_insert_rowid()", Long.class));
            genre.setId(genreId.get());
        }
        else {
            template.update("INSERT INTO genres (id, name) VALUES (:id, :name) " +
                            "ON CONFLICT(id) DO UPDATE SET name = :name WHERE id = :id",
                    Map.of("id", genre.getId(), "name", genre.getName()));
        }
    }

    public void removeGenreById(long id) {
        template.update("DELETE FROM genres WHERE id = :id", Map.of("id", id));
    }
}
