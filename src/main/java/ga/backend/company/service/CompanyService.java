package ga.backend.company.service;

import ga.backend.company.entity.Company;
import ga.backend.company.repository.CompanyRepository;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRespository;

    // CREATE
    public Company createCompany(Company company) {
        return companyRespository.save(company);
    }

    // READ
    public Company findCompany(long companyPk) {
        Company company = verifiedCompany(companyPk);
        return company;
    }

    // UPDATE
    public Company patchCompany(Company company) {
        Company findCompany = verifiedCompany(company.getPk());
        Optional.ofNullable(company.getName()).ifPresent(findCompany::setName);

        return companyRespository.save(findCompany);
    }

    // DELETE
    public void deleteCompany(long companyPk) {
        Company company = verifiedCompany(companyPk);
        companyRespository.delete(company);
    }

    // 검증
    public Company verifiedCompany(long companyPk) {
        Optional<Company> company = companyRespository.findById(companyPk);
        return company.orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMPANY_NOT_FOUND));
    }
}
