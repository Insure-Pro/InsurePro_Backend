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
import ga.backend.util.CustomerType;
import ga.backend.util.FindEmployee;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@AllArgsConstructor
public class AnalysisService {
    private final AnanlysisRepository analysisRespository;
    private final CustomerRepository customerRepository;
    private final ScheduleRepository scheduleRepository;
    private final FindEmployee findEmployee;

    private final List<CustomerType> customerTypesRegisterDate = List.of(
            CustomerType.OD,
            CustomerType.AD,
            CustomerType.CP,
            CustomerType.CD,
            CustomerType.JD
    );

    // CREATE
    public Analysis createAnalysis(Analysis analysis) {
        return analysisRespository.save(analysis);
    }

    // READ
    public Analysis findAnalysis(long analysisPk) {
        Analysis analysis = verifiedAnalysis(analysisPk);
        return analysis;
    }

    public Analysis findAnalysis(LocalDate requestDate, CustomerType customerType) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        Analysis analysis = verifiedAnalysis(employee, requestDate, customerType); // 이전에 구현된 analysis가 있는지 확인

        // 분석하기
        LocalDateTime start = requestDate.atStartOfDay().withDayOfMonth(1); // 요청한 달의 첫째낫 00:00:00
        LocalDateTime finish = requestDate.atTime(LocalTime.MAX).withDayOfMonth(requestDate.lengthOfMonth()); // 요청한 달의 마지막날 23:59:99
        analysisPercentage(start, finish, employee, analysis, customerType);
        analysisAllPercentage(start, finish, employee, analysis);

        // 다시 분석할지 여부 -> 구현할지 말지 고민중
        // 다시 분석할지 확인한 코드
        /*
        // 이번달일 경우 사용
        LocalDateTime now = LocalDateTime.now();

        // 시작 날짜 -> 1일 00:00
        LocalDateTime start = requestDate.atStartOfDay().withDayOfMonth(1);
        // 종료 날짜
        LocalDateTime finish;

        // 1. 계산이 되어있지 않은 경우 -> 이번달인것과 아닌것 구분 X
        // 2. 이번달인데, 계산이 되어 있는 경우

        // 성과분석이 안되어있는 달(이번달과 이전달 포함)
        if(analysis.getPk() == null) {
            finish = requestDate.atTime(LocalTime.MAX).withDayOfMonth(requestDate.lengthOfMonth()); // 마지막날 23:59:99
            // 성과분석 계산
            analysisAllPercentage(start, finish, employee, analysis, customerType);
        } else if (checkMonth(requestDate, now)) { // 종료날짜 -> 요청날짜(requsetDate) == 이번달(now)
            if (checkAnalysis(employee, analysis)) { // 수정해야 하는 경우 -> customer과 history 변경사항 발생 시
                finish = now; // 종료 날짜 -> 지금
                // 성과분석 계산
                analysisAllPercentage(start, finish, employee, analysis, customerType);
            }
        } else if(analysis.getModifiedAt().isBefore(start.plusMonths(1))) { // 이전 달의 성과분석 업데이트를 해야하는 경우(수정날짜 < 다음달 1일 00:00)
            finish = requestDate.atTime(LocalTime.MAX).withDayOfMonth(requestDate.lengthOfMonth());
            // 성과분석 계산
            analysisAllPercentage(start, finish, employee, analysis, customerType);
        }
         */

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

    public Analysis verifiedAnalysis(Employee employee, LocalDate requestDate, CustomerType customerType) {
        Optional<Analysis> optionalAnalysis = analysisRespository.findByEmployeeAndDateAndCustomerType(
                employee,
                requestDate.withDayOfMonth(1),
                customerType
        );

        // requestDate가 존재하지 않은 경우
        return optionalAnalysis.orElseGet(() -> {
            Analysis analysis = new Analysis();
            analysis.setEmployee(employee); // 직원 설정
            analysis.setCustomerType(customerType); // 고객유형 설정
            analysis.setDate(requestDate.withDayOfMonth(1)); // 성과분석 날짜 설정
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
        List<Customer> customers = customerRepository.findByEmployeeAndCreatedAtGreaterThanEqualOrModifiedAtGreaterThanEqual(
                employee, analysis.getModifiedAt(), analysis.getModifiedAt()
        );
        customers.forEach(customer -> System.out.println(customer.getPk()));
        if (!customers.isEmpty()) return true;

        // 새로 추가되거나 수정된 history이 있는지 확인
        List<Schedule> schedules = scheduleRepository.findByEmployeeAndCreatedAtGreaterThanEqualOrModifiedAtGreaterThanEqual(
                employee, analysis.getModifiedAt(), analysis.getModifiedAt()
        );
        return !schedules.isEmpty();
    }

    // 성과분석 계산
    public Analysis analysisPercentage(LocalDateTime start, LocalDateTime finish, Employee employee, Analysis analysis, CustomerType customerType) {
        // 이번달에 DB에 등록한 고객들
        List<Customer> customers = customerRepository.findByEmployeeAndRegisterDateBetweenAndCustomerTypeAndDelYnFalse(
                employee, start.toLocalDate(), finish.toLocalDate(), customerType
        );
        double customer_count = customers.size();

        if(customer_count == 0) { // 이번달에 DB에 등록한 고객들이 없는 경우
            analysis.setTARatio(0.0); // TA 확률
            analysis.setAPRatio(0.0); // AP 확률
            analysis.setPCRatio(0.0); // PC 확률
            analysis.setTA(0L); // TA 개수
            analysis.setAP(0L); // AP 개수
            analysis.setPC(0L); // PC 개수
            analysis.setSubscriptionCount(0); // 청약 개수
        } else { // 이번달에 DB에 등록한 고객들이 있는 경우
            Map<String, Long> schedule_count = progressFilter(customers); // 이번달에 등록된 히스토리별 고객들(Progree별)
            analysis.setTARatio(schedule_count.get("TA") / customer_count); // TA 확률
            analysis.setAPRatio(schedule_count.get("AP") / customer_count); // AP 확률
            analysis.setPCRatio(schedule_count.get("PC") / customer_count); // PC 확률
            analysis.setTA(schedule_count.get("TA")); // TA 개수
            analysis.setAP(schedule_count.get("AP")); // AP 개수
            analysis.setPC(schedule_count.get("PC")); // PC 개수
            analysis.setSubscriptionCount(isContractYnCount(customers)); // 청약 개수
        }

        return analysisRespository.save(analysis);
    }

    // 성과분석 계산(ALL)
    public Analysis analysisAllPercentage(LocalDateTime start, LocalDateTime finish, Employee employee, Analysis analysis) {
        // 이번달에 DB에 등록한 고객들
        List<Customer> customers = customerRepository.findAllByEmployeeAndRegisterDateBetweenAndCustomerTypeInAndDelYnFalse(
                employee, start.toLocalDate(), finish.toLocalDate(), customerTypesRegisterDate
        );
        double customer_count = customers.size();

        System.out.println("!! customerCount : " + customer_count);
        System.out.println("!! : " + isExistHistory(customers));

        if(customer_count == 0) { // 이번달에 DB에 등록한 고객들이 없는 경우
            analysis.setAllTARatio(0.0); // 모든 TA 확률
            analysis.setAllAPRatio(0.0); // 모든 AP 확률
            analysis.setAllPCRatio(0.0); // 모든 PC 확률
            analysis.setAllHistoryRatio(0.0); // history 비율
        } else { // 이번달에 DB에 등록한 고객들이 있는 경우
            Map<String, Long> schedule_count = progressFilter(customers); // 이번달에 등록된 히스토리별 고객들(Progree별)
            analysis.setAllTARatio(schedule_count.get("TA") / customer_count); // 모든 TA 확률
            analysis.setAllAPRatio(schedule_count.get("AP") / customer_count); // 모든 AP 확률
            analysis.setAllPCRatio(schedule_count.get("PC") / customer_count); // 모든 PC 확률
            analysis.setAllHistoryRatio(isExistHistory(customers) / customer_count); // history 비율

            System.out.println("!! count : " + schedule_count.get("TA"));
            System.out.println("!! count : " + schedule_count.get("AP"));
            System.out.println("!! count : " + schedule_count.get("PC"));
        }

        // 청약확률
        analysis.setSubscriptionCount(isContractYnCount(customers));

        return analysisRespository.save(analysis);
    }

    // Progress별 Schedule 개수세기(TA, AP, PC)
    public Map<String, Long> progressFilter(List<Customer> customers) {
        // 고객별 TA, AP, PC는 1개만 세기

        Set<Customer> TA = new HashSet<>();
        Set<Customer> AP = new HashSet<>();
        Set<Customer> PC = new HashSet<>();

        customers.forEach(customer -> {
            customer.getSchedules().forEach(
                    schedule -> {
                        if(schedule.getProgress().getValue().equals("TA")) TA.add(customer);
                        if(schedule.getProgress().getValue().equals("AP")) AP.add(customer);
                        if(schedule.getProgress().getValue().equals("PC")) PC.add(customer);
                    }
            );
        });

        Map<String, Long> map = new HashMap<>();
        map.put("TA", (long) TA.size());
        map.put("AP", (long) AP.size());
        map.put("PC", (long) PC.size());

        return map;
    }

    // 계약을 한 고객들 count
    public int isContractYnCount(List<Customer> customers) {
        return (int) customers.stream().filter(Customer::getContractYn).count();
    }

    // history가 없는 고객들 count
    public double isExistHistory(List<Customer> customers) {
        return customers.stream().filter(customer -> customer.getSchedules().isEmpty()).count();
    }
}