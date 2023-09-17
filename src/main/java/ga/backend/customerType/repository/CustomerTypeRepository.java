package ga.backend.customerType.repository;

import ga.backend.company.entity.Company;
import ga.backend.customerType.entity.CustomerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerTypeRepository extends JpaRepository<CustomerType, Long> {

    Optional<CustomerType> findTopByDelYnAndType(boolean delYn, String type);
    List<CustomerType> findAllByDelYnAndCompany_Pk(boolean delYn, Long pk);
    List<CustomerType> findAllByDelYnAndCompany_Name(boolean delYn, String name);
    List<CustomerType> findAllByDelYn(boolean delYn);
    List<CustomerType> findAllByDelYnAndCompany_PkAndCompany_Name(boolean delYn, Long pk, String name);
}
