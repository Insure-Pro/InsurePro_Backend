package ga.backend.contract.service;

import ga.backend.contract.entity.Contract;
import ga.backend.contract.repository.ContractRepository;
import ga.backend.customer.entity.Customer;
import ga.backend.customer.service.CustomerService;
import ga.backend.employee.entity.Employee;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.schedule.entity.Schedule;
import ga.backend.schedule.service.ScheduleService;
import ga.backend.util.FindEmployee;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ContractService {
    private final ContractRepository contractRespository;
    private final CustomerService customerService;
    private final ScheduleService scheduleService;
    private final FindEmployee findEmployee;

    // CREATE
    public Contract createContract(Contract contract, Long customerPk, Long schedulePk) {
        setCustomerAndSchedule(contract, customerPk, schedulePk);

        return contractRespository.save(contract);
    }

    // READ
    public Contract findContract(long contractPk) {
        return verifiedContract(contractPk);
    }

    //  Customer별 contract 리스트 조회
    public List<Contract> findContractByCustomerPk(long customerPk) {
        Customer customer = customerService.findCustomer(customerPk);
        return contractRespository.findByCustomer(customer);
    }

    // UPDATE
    public Contract patchContract(Contract contract, Long customerPk, Long schedulePk) {
        Contract findContract = verifiedContract(contract.getPk());

        setCustomerAndSchedule(findContract, customerPk, schedulePk); // 외래키 설정
        Optional.ofNullable(contract.getName()).ifPresent(findContract::setName);
        Optional.ofNullable(contract.getMemo()).ifPresent(findContract::setMemo);
        Optional.ofNullable(contract.getContractDate()).ifPresent(findContract::setContractDate);
        return contractRespository.save(findContract);
    }

    // DELETE
    public void deleteContract(long contractPk) {
        Contract contract = verifiedContract(contractPk);
        contractRespository.delete(contract);
    }

    // schedule에서 contract 삭제
    public void deleteContractBySchedule(long contractPk) {
        Contract contract = verifiedContract(contractPk);
        contract.setSchedule(null);
        contractRespository.save(contract);
    }

    // 검증
    public Contract verifiedContract(long contractPk) {
        Optional<Contract> contract = contractRespository.findById(contractPk);
        return contract.orElseThrow(() -> new BusinessLogicException(ExceptionCode.CONTRACT_NOT_FOUND));
    }

    // 외래키 설정
    public void setCustomerAndSchedule(Contract contract, Long customerPk, Long schedulePk) {
        Employee employee = findEmployee.getLoginEmployeeByToken();

        if(schedulePk != null) {
            Schedule schedule = scheduleService.findSchedule(schedulePk);
            if (!Objects.equals(schedule.getEmployee().getPk(), employee.getPk()))
                throw new BusinessLogicException(ExceptionCode.SCHEDULE_AND_EMPLOYEE_NOT_MATCH);
            contract.setSchedule(schedule);
            contract.setCustomer(schedule.getCustomer());
        }

        if (customerPk != null) {
            Customer customer = customerService.findCustomer(customerPk);
            if (!Objects.equals(customer.getEmployee().getPk(), employee.getPk()))
                throw new BusinessLogicException(ExceptionCode.CUSTOMER_AND_EMPLOYEE_NOT_MATCH);
            contract.setCustomer(customer);
        }

        // 유효성 확인(schedule, customer)
        if(contract.getSchedule() != null) {
            if (!Objects.equals(contract.getCustomer().getPk(), contract.getSchedule().getCustomer().getPk()))
                throw new BusinessLogicException(ExceptionCode.SCHEDULE_AND_CUSTOMER_NOT_MATCH);
        }
    }
}
