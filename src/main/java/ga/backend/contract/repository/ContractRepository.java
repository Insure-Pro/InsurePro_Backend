package ga.backend.contract.repository;

import ga.backend.contract.entity.Contract;
import ga.backend.customer.entity.Customer;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.employee.entity.Employee;
import ga.backend.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByCustomer(Customer customer);
    List<Contract> findBySchedule(Schedule schedule);
    List<Contract> findByCustomerAndScheduleIsNull(Customer customer);
    long countByCustomerEmployeeAndContractDateBetweenAndCustomerCustomerType(Employee employee, LocalDate start, LocalDate finish, CustomerType customerType);// 성과분석

}
