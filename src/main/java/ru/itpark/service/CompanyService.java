package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itpark.domain.ProductionCompany;
import ru.itpark.repository.CompanyRepository;

import java.util.List;

import static ru.itpark.Constants.LIST_SIZE;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    public long getCompanyRepoSize() {
        return companyRepository.size();
    }

    public List<ProductionCompany> getCompanies(int number) {
        int offset = LIST_SIZE*(number-1);
        return companyRepository.getCompanies(offset);
    }

    public ProductionCompany getCompanyById(long id) {
        return companyRepository.getCompanyById(id);
    }

    public long getCompanyIdByName(String name) {
        return companyRepository.getCompanyIdByName(name);
    }

    public void saveCompany(ProductionCompany company) {
        companyRepository.saveCompany(company);
    }

    public void removeCompanyById(long id) {
        companyRepository.removeCompanyById(id);
    }
}
