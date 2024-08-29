package ga.backend.analysis.entity;

import ga.backend.auditable.Auditable;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.employee.entity.Employee;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Analysis extends Auditable { // 성과분석
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_pk")
    private Long pk; // 식별자

    @ManyToOne
    @JoinColumn(name = "employee_pk")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "customer_type_pk")
    private CustomerType customerType;

    @Column
    private LocalDate date; // 성과분석(년-월-01)

    // 이번달에 추가된 customer 개수
    @Column
    private int dbCustomerCount; // DB 고객유형

    @Column
    private int etcCustomerCount; // ETC 고객유형

    // customer의 상담현황 확률
    @Column
    private double beforeConsultationRatio; // 상담전

    @Column
    private double pendingCounsultationRatio; // 상담보류중

    @Column
    private double productProposalRatio; // 상품제안중

    @Column
    private double medicalHistoryWaitingRatio; // 병력대기

    @Column
    private double subscriptionRejectionRatio; // 청약거절

    @Column
    private double consultationRejectionRatio; // 상담거절

    // Contract의 계약 체결한 Contract 개수
    @Column
    private int contractCount;

    // 이번달에 계약을 체결한 Customer 개수
    @Column
    private int contractYnCount;

    // Customer의 상담현황 = AS_TARGET인 Customer 개수
    @Column
    private int asTargetCount;

    // TA의 Customer 개수
    @Column
    private int absenceCount; // 부재

    @Column
    private int rejectionCount; // 거절

    @Column
    private int pendingCount; // 보류

    @Column
    private int promiseCount; // 확약

    // Schedule의 Progress(진척도)의 Customer 개수
    @Column
    private int apCount; // 초회상담

    @Column
    private int icCount; // 정보수집

    @Column
    private int pcCount; // 상품제안

    @Column
    private int cicCount; // 계약체결

    @Column
    private int stCount; // 증권전달

    @Column
    private int ocCount; // 기타상담
}