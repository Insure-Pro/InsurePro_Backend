package ga.backend.customerType.repository;

import ga.backend.customerType.entity.CustomerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerTypeRepository extends JpaRepository<CustomerType, Long> {
    Optional<CustomerType> findByPkAndDelYnFalse(long customerTypePk);
}
