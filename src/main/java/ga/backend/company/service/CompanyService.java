package ga.backend.company.service;

import ga.backend.company.entity.Company;
import ga.backend.company.repository.CompanyRepository;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    public List<Company> findCompanys(Long pk, String name) {

        List<Company> companyList;

        if (pk == null && name == null) {
            companyList = companyRespository.findAllByDelYn(false);
        } else if (pk == null) {
            companyList = companyRespository.findAllByDelYnAndName(false, name);
        } else if (name == null) {
            companyList = companyRespository.findAllByDelYnAndPk(false, pk);
        } else {
            companyList = companyRespository.findAllByDelYnAndPkAndName(false, pk, name);
        }
        return companyList;
    }

    public Company findCompany(long companyPk) {
        return verifiedCompany(companyPk);
    }

    // UPDATE
    public Company patchCompany(Company company) {
        Company findCompany = verifiedCompany(company.getPk());
        Optional.ofNullable(company.getName()).ifPresent(findCompany::setName);

        return companyRespository.save(findCompany);
    }

    // DELETE
    public Company deleteCompany(long companyPk) {
        Company company = verifiedCompany(companyPk);
        company.setDelYn(true);
        companyRespository.save(company);
        return company;
    }

    // 검증
    public Company verifiedCompany(long companyPk) {
        Optional<Company> company = companyRespository.findById(companyPk);
        return company.orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMPANY_NOT_FOUND));
    }
}
