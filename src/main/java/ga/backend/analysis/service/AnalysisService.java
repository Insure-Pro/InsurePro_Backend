package ga.backend.analysis.service;

import ga.backend.analysis.entity.Analysis;
import ga.backend.analysis.repository.AnanlysisRepository;
import ga.backend.contract.repository.ContractRepository;
import ga.backend.customer.entity.ConsultationStatus;
import ga.backend.customer.entity.Customer;
import ga.backend.customer.repository.CustomerRepository;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.customerType.service.CustomerTypeService;
import ga.backend.employee.entity.Employee;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.schedule.entity.Progress;
import ga.backend.schedule.entity.Schedule;
import ga.backend.schedule.repository.ScheduleRepository;
import ga.backend.ta.entity.Status;
import ga.backend.ta.entity.TA;
import ga.backend.ta.repository.TARepository;
import ga.backend.util.FindEmployee;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AnalysisService {
    private final AnanlysisRepository analysisRespository;
    private final CustomerRepository customerRepository;
    private final ScheduleRepository scheduleRepository;
    private final ContractRepository contractRepository;
    private final TARepository taRepository;
    private final CustomerTypeService customerTypeService;
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

    public Analysis findAnalysis(LocalDate requestDate, long customerTypePk) {
        Employee employee = findEmployee.getLoginEmployeeByToken();

        // 이전에 구현된 analysis가 있는지 확인
        LocalDate date = requestDate.withDayOfMonth(1); // 요청한 달의 첫째날
        Analysis analysis = verifiedAnalysis(employee, date, customerTypePk);

        // 계산여부 확인 -> 요청한 달이 이번달이면 계산
        if (checkMonth(date)) calculateAnalysis(analysis, employee, customerTypePk, requestDate);

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

    // 검증
    public Analysis verifiedAnalysis(Employee employee, LocalDate date, long customerTypePk) {
        CustomerType customerType = customerTypeService.findCustomerType(customerTypePk);

        Optional<Analysis> optionalAnalysis = analysisRespository.findByEmployeeAndDateAndCustomerType(
                employee,
                date,
                customerType
        );

        // requestDate가 존재하지 않은 경우
        return optionalAnalysis.orElseGet(() -> {
            Analysis analysis = new Analysis();
            analysis.setEmployee(employee); // 직원 설정
            analysis.setCustomerType(customerType); // 고객유형 설정
            analysis.setDate(date); // 성과분석 날짜 설정
            return analysis;
        });
    }

    // 요청 날짜가 이번달인지 확인
    // true : 요청날짜 == 이번달(다시 계산), false : 요청날짜 != 이번달(다시 계산X)
    public boolean checkMonth(LocalDate date) {
        boolean check = true;
        LocalDate now = LocalDate.now();

        if (now.getMonthValue() != date.getMonthValue() || now.getYear() != date.getYear()) check = false;
        return check;
    }

    // 성과분석 계산
    public void calculateAnalysis(Analysis analysis, Employee employee, long customerTypePk, LocalDate requestDate) {
        // 분석하는 날짜
        LocalDateTime start = requestDate.atStartOfDay().withDayOfMonth(1); // 요청한 달의 첫째날 00:00:00
        LocalDateTime finish = requestDate.atTime(LocalTime.MAX).withDayOfMonth(requestDate.lengthOfMonth()); // 요청한 달의 마지막날 23:59:99
        LocalDate startDate = requestDate.withDayOfMonth(1); // 요청한 달의 첫째날
        LocalDate finishDate = requestDate.withDayOfMonth(requestDate.lengthOfMonth()); // 요청한 달의 마지막날

        CustomerType customerType = customerTypeService.findCustomerType(customerTypePk);

        // 이번달에 추가된 customer 개수
        if (customerTypeService.dataTypeisDB(customerType)) // DB 고객유형
            analysis.setDbCustomerCount(
                    (int) customerRepository.countByEmployeeAndRegisterDateBetweenAndDelYnFalseAndCustomerType(
                            employee, startDate, finishDate, customerType
                    ));
        else // ETC 고객유형
            analysis.setEtcCustomerCount((
                    int) customerRepository.countByEmployeeAndCreatedAtBetweenAndDelYnFalseAndCustomerType(
                    employee, start, finish, customerType
            ));

        // customer의 상담현황 확률
        consultationStatusRatio(analysis, employee, start, finish, customerType);

        // Contract의 계약 체결한 Contract 개수
        analysis.setContractCount((int) contractRepository.countByCustomerEmployeeAndContractDateBetweenAndCustomerCustomerType(
                employee,
                startDate,
                finishDate,
                customerType
        ));

        // TA의 Customer 개수
        taCustomerCount(analysis, employee, startDate, finishDate, customerType);

        // Schedule의 Progress(진척도)의 Customer 개수
        scheduleCustomerCount(analysis, employee, startDate, finishDate, customerType);
    }

    // customer의 상담현황 확률
    public void consultationStatusRatio(Analysis analysis, Employee employee, LocalDateTime start, LocalDateTime finish, CustomerType customerType) {
        List<Customer> allCustomersByConsultationStatusModifiedAt = customerRepository.findByEmployeeAndConsultationStatusModifiedAtBetweenAndCustomerTypeAndDelYnFalse(
                employee,
                start,
                finish,
                customerType
        );
        double allCustomerCount = allCustomersByConsultationStatusModifiedAt.size();
        if(allCustomerCount == 0) { // NaN 방지
            analysis.setBeforeConsultationRatio(0.0);
            analysis.setPendingCounsultationRatio(0.0);
            analysis.setProductProposalRatio(0.0);
            analysis.setMedicalHistoryWaitingRatio(0.0);
            analysis.setSubscriptionRejectionRatio(0.0);
            analysis.setConsultationRejectionRatio(0.0);
        } else {
            int beforeConsultationCount = 0;
            int pendingConsultationCount = 0;
            int productProposalCount = 0;
            int medicalHistoryWaitingCount = 0;
            int subscriptionRejectionCount = 0;
            int consultationRejectionCount = 0;
            int asTargetCount = 0;
            for (Customer customer : allCustomersByConsultationStatusModifiedAt) {
                if (customer.getConsultationStatus() == ConsultationStatus.BEFORE_CONSULTATION) beforeConsultationCount++;
                else if (customer.getConsultationStatus() == ConsultationStatus.PENDING_CONSULTATION) pendingConsultationCount++;
                else if (customer.getConsultationStatus() == ConsultationStatus.PRODUCT_PROPOSAL) productProposalCount++;
                else if (customer.getConsultationStatus() == ConsultationStatus.MEDICAL_HISTORY_WAITING) medicalHistoryWaitingCount++;
                else if (customer.getConsultationStatus() == ConsultationStatus.SUBSCRIPTION_REJECTION) subscriptionRejectionCount++;
                else if (customer.getConsultationStatus() == ConsultationStatus.CONSULTATION_REJECTION) consultationRejectionCount++;
                else if (customer.getConsultationStatus() == ConsultationStatus.AS_TARGET) asTargetCount++;
            }
            analysis.setBeforeConsultationRatio(beforeConsultationCount / allCustomerCount);
            analysis.setPendingCounsultationRatio(pendingConsultationCount / allCustomerCount);
            analysis.setProductProposalRatio(productProposalCount / allCustomerCount);
            analysis.setMedicalHistoryWaitingRatio(medicalHistoryWaitingCount / allCustomerCount);
            analysis.setSubscriptionRejectionRatio(subscriptionRejectionCount / allCustomerCount);
            analysis.setConsultationRejectionRatio(consultationRejectionCount / allCustomerCount);
            analysis.setAsTargetCount(asTargetCount); // Customer의 상담현황 = AS_TARGET인 Customer 개수
        }
    }

    // TA의 Customer 개수
    public void taCustomerCount(Analysis analysis, Employee employee, LocalDate startDate, LocalDate finishDate, CustomerType customerType) {
        List<TA> tas = new ArrayList<>(taRepository.findByEmployeeAndDateBetweenAndDelYnFalseAndCustomerCustomerTypeOrderByDateDescTimeDescPkDesc(
                        employee,
                        startDate,
                        finishDate,
                        customerType
                ).stream()
                .collect(Collectors.toMap(
                        TA::getCustomer,
                        ta -> ta,
                        (existing, replacement) -> existing
                )).values());

        int absenceCount = 0;
        int rejectionCount = 0;
        int pendingCount = 0;
        int promiseCount = 0;
        for(TA ta : tas) {
            if(ta.getStatus() == Status.ABSENCE) absenceCount++;
            else if(ta.getStatus() == Status.REJECTION) rejectionCount++;
            else if(ta.getStatus() == Status.PENDING) pendingCount++;
            else if(ta.getStatus() == Status.PROMISE) promiseCount++;
        }
        analysis.setAbsenceCount(absenceCount);
        analysis.setRejectionCount(rejectionCount);
        analysis.setPendingCount(pendingCount);
        analysis.setPromiseCount(promiseCount);
    }

    // Schedule의 Progress(진척도)의 Customer 개수
    public void scheduleCustomerCount(Analysis analysis, Employee employee, LocalDate startDate, LocalDate finishDate, CustomerType customerType) {
        List<Schedule> schedules = new ArrayList<>(scheduleRepository.findByEmployeeAndDateBetweenAndDelYnFalseAndCustomerCustomerTypeOrderByDateDescPkDesc(
                        employee,
                        startDate,
                        finishDate,
                        customerType
                ).stream()
                .collect(Collectors.toMap(
                        Schedule::getCustomer,
                        schedule -> schedule,
                        (existing, replacement) -> existing
                )).values());
        int apCount = 0;
        int icCount = 0;
        int pcCount = 0;
        int cicCount = 0;
        int stCount = 0;
        int ocCount = 0;
        for(Schedule schedule : schedules) {
            if(schedule.getProgress() == Progress.AP) apCount++;
            else if(schedule.getProgress() == Progress.IC) icCount++;
            else if(schedule.getProgress() == Progress.PC) pcCount++;
            else if(schedule.getProgress() == Progress.CIC) cicCount++;
            else if(schedule.getProgress() == Progress.ST) stCount++;
            else if(schedule.getProgress() == Progress.OC) ocCount++;
        }
        analysis.setApCount(apCount);
        analysis.setIcCount(icCount);
        analysis.setPcCount(pcCount);
        analysis.setCicCount(cicCount);
        analysis.setStCount(stCount);
        analysis.setOcCount(ocCount);

        analysisRespository.save(analysis);
    }
}