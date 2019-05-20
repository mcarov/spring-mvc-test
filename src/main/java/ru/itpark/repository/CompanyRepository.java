package ru.itpark.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itpark.domain.ProductionCompany;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

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
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT)");
    }

    public int size() {
        try {
            return template.getJdbcTemplate().queryForObject("SELECT COUNT(*) FROM companies", Integer.class);
        }
        catch(NullPointerException e) {
            return 0;
        }
    }

    public List<ProductionCompany> getCompanies(int offset) {
        return template.query("SELECT id, name FROM companies ORDER BY id LIMIT :offset, :limit",
                Map.of("offset", offset, "limit", LIST_SIZE), rowMapper);
    }

    public ProductionCompany getCompanyById(long id) {
        List<ProductionCompany> list = template.query("SELECT id, name FROM companies WHERE id = :id", Map.of("id", id), rowMapper);
        return list.get(0);
    }

    public void save(ProductionCompany company) {
        if(company.getId() == 0) {
            template.update("INSERT INTO companies (id, name) VALUES (:id, :name)",
                    Map.of("id", company.getId(), "name", company.getName()));
        }
        else {
            template.update("INSERT INTO companies (id, name) VALUES (:id, :name) " +
                            "ON CONFLICT(id) DO UPDATE SET name = :name WHERE id = :id",
                    Map.of("id", company.getId(), "name", company.getName()));
        }
    }
}
