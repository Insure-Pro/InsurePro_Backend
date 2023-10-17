package ga.backend.analysis.service;

import ga.backend.analysis.entity.Analysis;
import ga.backend.analysis.repository.AnanlysisRepository;
import ga.backend.customer.entity.Customer;
import ga.backend.customer.repository.CustomerRepository;
import ga.backend.employee.entity.Employee;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.schedule.entity.Schedule;
import ga.backend.schedule.repository.ScheduleRepository;
import ga.backend.util.FindEmployee;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AnalysisService {
    private final AnanlysisRepository analysisRespository;
    private final CustomerRepository customerRepository;
    private final ScheduleRepository scheduleRepository;
    private final FindEmployee findEmployee;

    // CREATE
    public Analysis createAnalysis(Analysis analysis) {
        return analysisRespository.save(analysis);
    }

    // READ
    public Analysis findAnalysis(long analysisPk) {
        Analysis analysis = verifiedAnalysis(analysisPk);
        return analysis;
    }

    public Analysis findAnalysis(LocalDate requestDate, Customer.CustomerType customerType) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        Analysis analysis = verifiedAnalysis(employee, requestDate, customerType);
        LocalDateTime now = LocalDateTime.now(); // 이번달일 경우 사용
        System.out.println("now : " + now);

        // 시작 날짜 -> 1일 00:00
        LocalDateTime start = requestDate.atStartOfDay().withDayOfMonth(1);
        // 종료 날짜
        LocalDateTime finish;

        // 1. 계산이 되어있지 않은 경우 -> 이번달인것과 아닌것 구분 X
        // 2. 이번달인데, 계산이 되어 있는 경우

        // 성과분석이 안되어있는 달(이번달과 이전달 포함)
        if(analysis.getPk() == null) {
            finish = requestDate.atTime(LocalTime.MAX).withDayOfMonth(requestDate.lengthOfMonth()); // 마지막날 23:59:99
            System.out.println("!! finish : " + finish);
            // 성과분석 계산
            analysisAllPercentage(start, finish, employee, analysis, customerType);
        } else if (checkMonth(requestDate, now)) { // 종료날짜 -> 요청날짜(requsetDate) == 이번달(now)
            if (checkAnalysis(employee, analysis)) { // 수정해야 하는 경우 -> customer과 history 변경사항 발생 시
                finish = now; // 종료 날짜 -> 지금
                System.out.println("!! finish : " + finish);
                // 성과분석 계산
                analysisAllPercentage(start, finish, employee, analysis, customerType);
            }
        } else if(analysis.getModifiedAt().isBefore(start.plusMonths(1))) { // 이전 달의 성과분석 업데이트를 해야하는 경우(수정날짜 < 다음달 1일 00:00)
            finish = requestDate.atTime(LocalTime.MAX).withDayOfMonth(requestDate.lengthOfMonth());
            System.out.println("!! finish(이전달) : " + finish);
            // 성과분석 계산
            analysisAllPercentage(start, finish, employee, analysis, customerType);
        }

        return analysis;
    }

    // UPDATE
    public Analysis patchAnalysis(Analysis analysis) {
        Analysis findAnalysis = verifiedAnalysis(analysis.getPk());
//        Optional.ofNullable(analysis.getName()).ifPresent(findAnalysis::setName);

        return analysisRespository.save(findAnalysis);
    }

    // 검증
    public Analysis verifiedAnalysis(long analysisPk) {
        Optional<Analysis> analysis = analysisRespository.findById(analysisPk);
        return analysis.orElseThrow(() -> new BusinessLogicException(ExceptionCode.ANALYSIS_NOT_FOUND));
    }

    public Analysis verifiedAnalysis(Employee employee, LocalDate requestDate, Customer.CustomerType customerType) {
        Optional<Analysis> optionalAnalysis = analysisRespository.findByEmployeeAndDateAndCustomerType(
                employee,
                requestDate.withDayOfMonth(1),
                customerType
        );

        System.out.println("!! isEmpty : " + optionalAnalysis.isEmpty());

        // requestDate가 존재하지 않은 경우
        return optionalAnalysis.orElseGet(() -> {
            Analysis analysis = new Analysis();
            analysis.setEmployee(findEmployee.getLoginEmployeeByToken());
            analysis.setCustomerType(customerType);
            System.out.println("!! is Empty");
            return analysis;
        });
    }

    // 요청 날짜가 이번달인지 확인
    // true : 요청날짜 == 이번달, false : 요청날짜 != 이번달
    public boolean checkMonth(LocalDate requestDate, LocalDateTime now) {
        boolean check = true;

        if (now.getMonthValue() != requestDate.getMonthValue()) check = false;
        else if (now.getYear() != requestDate.getYear()) check = false;
//        System.out.println("!! check : " + check);
//        System.out.println("now.getDayOfMonth() : " + now.getMonthValue() + " + requestDate.getDayOfMonth() ; " + requestDate.getMonthValue());
//        System.out.println("now.getYear() : " + now.getYear() + " + requestDate.getYear() : " + requestDate.getYear());
        return check;
    }

    // 이번달일 경우 → 다시 계산해야하는지 확인
    // true : 다시 계산, false ; 계산 X
    public boolean checkAnalysis(Employee employee, Analysis analysis) {
        // now 이후 createdAt, modifiedAt된 customer과 history가 있으면 다시 계산하기

        // 새로 추가되거나 수정된 customer이 있는지 확인
//        List<Customer> customers = customerRepository.findByEmployeeAndModifiedAtGreaterThanEqual(
        List<Customer> customers = customerRepository.findByEmployeeAndCreatedAtGreaterThanEqualOrModifiedAtGreaterThanEqual(
                employee, analysis.getModifiedAt(), analysis.getModifiedAt()
//                employee, LocalDateTime.parse("2023-10-10T22:08:59.20977"), LocalDateTime.parse("2023-10-10T22:08:59.20977")
        );
        customers.forEach(customer -> System.out.println(customer.getPk()));
        System.out.println("!! analysis customer check : " + customers.size());
        if (!customers.isEmpty()) return true;

        // 새로 추가되거나 수정된 history이 있는지 확인
        List<Schedule> schedules = scheduleRepository.findByEmployeeAndCreatedAtGreaterThanEqualOrModifiedAtGreaterThanEqual(
                employee, analysis.getModifiedAt(), analysis.getModifiedAt()
        );
        System.out.println("!! analysis schedules check : " + schedules.size());
        return !schedules.isEmpty();
    }

    // 성과분석 계산
    public Analysis analysisAllPercentage(LocalDateTime start, LocalDateTime finish, Employee employee, Analysis analysis, Customer.CustomerType customerType) {
        // 전체(이번달에 등록한 고객 기준 고객수. 히스토리 등록 안 한 사람도 포함)
        double all = customerRepository.findByEmployeeAndRegisterDateBetweenAndCustomerTypeAndDelYnFalse(
                employee, start.toLocalDate(), finish.toLocalDate(), customerType
        ).size();
        System.out.println("!! all : " + all);

        // 전체 고객 대상으로 이번달에 등록한 히스토리
        List<Schedule> all_history = scheduleRepository.findByEmployeeAndCreatedAtBetweenAndCustomerCustomerTypeAndDelYnFalse(
                employee, start, finish, customerType
        );
        // 전체 고객 대상으로 이번달에 등록한 히스토리 총 개수 합
        double all_history_count = all_history.size();
        System.out.println("!! all_history_count : " + all_history_count);

        // 전체 대비 TA 확률
        List<Schedule> TA = progressFilter(all_history, Schedule.Progress.TA);  // 이번달에 등록된 히스토리 유형 TA
        double TA_count = TA.size();
        analysis.setAllTAPercentage(all != 0 ? TA_count / all : 0);
        System.out.println("!! TA_count : " + TA_count);

        // 전체 대비 AP 확률
        List<Schedule> AP = progressFilter(all_history, Schedule.Progress.AP); // 이번달에 등록된 히스토리 유형 AP
        double AP_count = AP.size();
        analysis.setAllAPPercentage(all != 0 ? AP_count / all : 0);
        System.out.println("!! AP_count : " + AP_count);

        // 전체 대비 PC 확률
        List<Schedule> PC = progressFilter(all_history, Schedule.Progress.PC); // 이번달에 등록된 히스토리 유형 PC
        double PC_count = PC.size();
        analysis.setAllPCPercentage(all != 0 ? PC_count / all : 0);
        System.out.println("!! PC_count : " + PC_count);

        // TA 진행확률
        analysis.setTAPercentage(analysis.getAllTAPercentage());

        // AP 진행확률
        analysis.setAPPercentage(TA_count != 0 ? AP_count / TA_count : 0);

        // PC 진행활률
        analysis.setPCPercentage(AP_count != 0 ? PC_count / AP_count : 0);

        // 전체 히스토리 TA 비율
        analysis.setAllTARatio(all_history_count != 0 ? customerFilter(TA).size() / all_history_count : 0);

        // 전체 히스토리 AP 비율
        analysis.setAllAPRatio(all_history_count != 0 ? customerFilter(AP).size() / all_history_count : 0);

        // 전체 히스토리 PC 비율
        Set<Customer> PC_customer = customerFilter(PC);
        analysis.setAllPCRatio(all_history_count != 0 ? PC_customer.size() / all_history_count : 0);
        System.out.println("!! PC_customer.size() : " + PC_customer.size());

        // 청약확률
//        int all_history_customer = customerFilter(all_history).size();
        analysis.setSubscriptionPercentage(!PC_customer.isEmpty() ? isContractYnCount(PC_customer) / PC_customer.size() : 0);

        // date 설정
        analysis.setDate(start.withDayOfMonth(1).toLocalDate());

        return analysisRespository.save(analysis);
    }

    // Progress별 Schedule 개수세기
    public List<Schedule> progressFilter(List<Schedule> schedules, Schedule.Progress progress) {
        return schedules.stream().filter(schedule -> schedule.getProgress() == progress)
                .collect(Collectors.toList());
    }

    // Schedule의 Customer 추출하기
    public Set<Customer> customerFilter(List<Schedule> schedules) {
        Set<Customer> set = new HashSet<>();
        schedules.stream().filter(schedule -> !schedule.getDelYn())
                .forEach(schedule -> set.add(schedule.getCustomer()));
        return set;
    }

    // 이번달에 PC 히스토리를 등록한 사람 중에서 계약을 한 고객을 count
    public double isContractYnCount(Set<Customer> customers) {
        return customers.stream().filter(Customer::getContractYn).count();
    }
}
