package ga.backend.ta.repository;

import ga.backend.customer.entity.Customer;
import ga.backend.ta.entity.TA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TARepository extends JpaRepository<TA, Long> {
    Optional<TA> findByPkAndDelYnFalse(Long pk);
    List<TA> findByCustomerAndDelYnFalse(Customer customer);
}