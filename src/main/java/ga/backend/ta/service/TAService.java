package ga.backend.ta.service;

import ga.backend.customer.entity.Customer;
import ga.backend.customer.service.CustomerService;
import ga.backend.employee.entity.Employee;
import ga.backend.employee.repository.EmployeeRepository;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.ta.entity.TA;
import ga.backend.ta.repository.TARepository;
import ga.backend.util.ConsultationStatus;
import ga.backend.util.FindEmployee;
import ga.backend.util.Status;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        if (ta.getTime() == null) ta.setTime(LocalTime.now());
        if (ta.getDate() == null) ta.setDate(LocalDate.now());

        // 부재 or 거절일 경우 Customer의 asCount 증가
        // 만약 "asCount"가 "asSetting"r보다 같거나 크다면 고객의 상담현황의 값을 "AS 대상"으로 변경하기
        if(customer.getCustomerType().getAsSetting() != null) { // asSetting값이 설정된 경우에만 counting하기
            if(ta.getStatus() == Status.ABSENCE || ta.getStatus() == Status.REJECTION) {
                // 부재 or 거절일 경우 Customer의 asCount 증가
                int asCount = customer.getAsCount()+1;
                customer.setAsCount(asCount);

                // 만약 "asCount"가 "asSetting"r보다 같거나 크다면 고객의 상담현황의 값을 "AS 대상"으로 변경하기
                if(asCount >= customer.getCustomerType().getAsSetting())
                    customer.setConsultationStatus(ConsultationStatus.AS_TARGET);
            }
        }

        return taRepository.save(ta);
    }

    // READ
    public TA findTA(long taPk) {
        return verifiedTA(taPk);
    }

    // 고객별 tas 조회
    public List<TA> findTAbyCustomerPk(long customerPk) {
        Customer customer = customerService.findCustomer(customerPk);
        return taRepository.findByCustomerAndDelYnFalse(customer);
    }

    // UPDATE
    public TA patchTA(TA ta) {
        TA findTa = verifiedTA(ta.getPk());
        Optional.ofNullable(ta.getCount()).ifPresent(findTa::setCount);
        Optional.ofNullable(ta.getTime()).ifPresent(findTa::setTime);
        Optional.ofNullable(ta.getDate()).ifPresent(findTa::setDate);
        Optional.ofNullable(ta.getMemo()).ifPresent(findTa::setMemo);
        Optional.ofNullable(ta.getStatus()).ifPresent(findTa::setStatus);

        return taRepository.save(findTa);
    }

    // DELETE
    public void deleteTA(long taPk) {
        TA ta = verifiedTA(taPk);
        ta.setDelYn(true);

        // TA 삭제 시 asCount 감소
        if(ta.getStatus() == Status.ABSENCE || ta.getStatus() == Status.REJECTION) {
            Customer customer = ta.getCustomer();
            customer.setAsCount(customer.getAsCount()-1);
        }

        taRepository.save(ta);
    }

    // 검증
    public TA verifiedTA(long taPk) {
        Optional<TA> ta = taRepository.findByPkAndDelYnFalse(taPk);
        return ta.orElseThrow(() -> new BusinessLogicException(ExceptionCode.TA_NOT_FOUND));
    }
}