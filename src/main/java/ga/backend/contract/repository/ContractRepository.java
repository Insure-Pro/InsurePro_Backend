package ga.backend.contract.repository;

import ga.backend.contract.entity.Contract;
import ga.backend.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByCustomer(Customer customer);
}
