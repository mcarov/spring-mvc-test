package ru.itpark.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itpark.domain.ProductionCompany;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ru.itpark.Constants.LIST_SIZE;

@Repository
public class CompanyRepository {
    private final NamedParameterJdbcTemplate template;

    private final RowMapper<ProductionCompany> rowMapper = (resultSet, i) -> {
        ProductionCompany productionCompany = new ProductionCompany();
        productionCompany.setId(resultSet.getLong(1));
        productionCompany.setName(resultSet.getString(2));
        return productionCompany;
    };

    public CompanyRepository(DataSource source) {
        this.template = new NamedParameterJdbcTemplate(source);
    }

    @PostConstruct
    public void init() {
        template.getJdbcTemplate().execute("CREATE TABLE IF NOT EXISTS companies(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT)");
    }

    public long size() {
        Optional<Long> size = Optional.ofNullable(template.getJdbcTemplate().queryForObject("SELECT COUNT(*) FROM companies", Long.class));
        return size.orElse(0L);
    }

    public List<ProductionCompany> getCompanies(int offset) {
        return template.query("SELECT id, name FROM companies ORDER BY id LIMIT :offset, :limit",
                Map.of("offset", offset, "limit", LIST_SIZE), rowMapper);
    }

    public ProductionCompany getCompanyById(long id) {
        return template.queryForObject("SELECT id, name FROM companies WHERE id = :id", Map.of("id", id), rowMapper);
    }

    public long getCompanyIdByName(String name) {
        List<Long> companylist = template.query("SELECT id FROM companies WHERE name LIKE :name",
                Map.of("name", name), (resultSet, i) -> resultSet.getLong(1));
        return companylist.isEmpty() ? 0 : companylist.get(0);
    }

    public void saveCompany(ProductionCompany company) {
        if(company.getId() == 0) {
            template.update("INSERT INTO companies (name) VALUES (:name)",
                    Map.of("name", company.getName()));

            Optional<Long> companyId = Optional.ofNullable(template.getJdbcTemplate().
                    queryForObject("SELECT last_insert_rowid()", Long.class));
            company.setId(companyId.get());
        }
        else {
            template.update("INSERT INTO companies (id, name) VALUES (:id, :name) " +
                            "ON CONFLICT(id) DO UPDATE SET name = :name WHERE id = :id",
                    Map.of("id", company.getId(), "name", company.getName()));
        }
    }

    public void removeCompanyById(long id) {
        template.update("DELETE FROM companies WHERE id = :id", Map.of("id", id));
    }
}
