package ga.backend.company.repository;

import ga.backend.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findAllByDelYnAndPk(boolean delYn, Long pk);
    List<Company> findAllByDelYnAndName(boolean delYn, String name);
    List<Company> findAllByDelYn(boolean delYn);
    List<Company> findAllByDelYnAndPkAndName(boolean delYn, Long pk, String name);
    Optional<Company> findByDelYnFalseAndPk(long companyPk);

}
