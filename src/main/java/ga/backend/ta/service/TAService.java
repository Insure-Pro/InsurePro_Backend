package ga.backend.ta.service;

import ga.backend.customer.entity.Customer;
import ga.backend.customer.service.CustomerService;
import ga.backend.employee.entity.Employee;
import ga.backend.employee.repository.EmployeeRepository;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.ta.entity.TA;
import ga.backend.ta.repository.TARepository;
import ga.backend.util.FindEmployee;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TAService {
    private final TARepository taRepository;
    private final FindEmployee findEmployee;
    private final CustomerService customerService;

    // CREATE
    public TA createTA(TA ta, Long customerPk) {
        // employee 설정
        Employee employee = findEmployee.getLoginEmployeeByToken();
        ta.setEmployee(employee);

        // customer 설정
        Customer customer = customerService.findCustomer(customerPk);
        ta.setCustomer(customer);

        // count 설정
        if (ta.getCount() == null) {
            // customer에 생성된 ta가 있는지 조회
            List<TA> tas = taRepository.findByCustomerAndDelYnFalse(customer);
            // count 값이 없고, customer이 있는 경우 : 이전 count + 1
            if (!tas.isEmpty()) ta.setCount(tas.get(tas.size() - 1).getCount() + 1);
            // count 값이 없고, customer이 없는 경우 : count = 1
            else ta.setCount(1);
        }

        // time 값이 없으면 지금 시각값을 넣음
        if (ta.getTime() == null) ta.setTime(LocalDateTime.now());

        return taRepository.save(ta);
    }

    // READ
    public TA findTA(long taPk) {
        return verifiedTA(taPk);
    }

    // UPDATE
    public TA patchTA(TA ta) {
        TA findTa = verifiedTA(ta.getPk());
        Optional.ofNullable(ta.getCount()).ifPresent(findTa::setCount);
        Optional.ofNullable(ta.getTime()).ifPresent(findTa::setTime);
        Optional.ofNullable(ta.getMemo()).ifPresent(findTa::setMemo);
        Optional.ofNullable(ta.getStatus()).ifPresent(findTa::setStatus);
        Optional.ofNullable(ta.getDelYn()).ifPresent(findTa::setDelYn);

        return taRepository.save(findTa);
    }

    // DELETE
    public void deleteTA(long taPk) {
        TA ta = verifiedTA(taPk);
        ta.setDelYn(true);
        taRepository.save(ta);
    }

    // 검증
    public TA verifiedTA(long taPk) {
        Optional<TA> ta = taRepository.findByPkAndDelYnFalse(taPk);
        return ta.orElseThrow(() -> new BusinessLogicException(ExceptionCode.TA_NOT_FOUND));
    }
}