package ga.backend.customerType.repository;

import ga.backend.company.entity.Company;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.employee.entity.Employee;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerTypeRepository extends JpaRepository<CustomerType, Long> {
    Optional<CustomerType> findByPkAndDelYnFalse(long customerTypePk);
    @Cacheable(value = "customerTypes", key = "#company.pk")
    List<CustomerType> findByCompanyAndDelYnFalse(Company company);
    Optional<CustomerType> findByNameAndDelYnFalse(String name);
}
