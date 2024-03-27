//package ga.backend.analysis.entity;
//
//import ga.backend.auditable.Auditable;
//import ga.backend.employee.entity.Employee;
//import ga.backend.customer.entity.CustomerTType;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import javax.persistence.*;
//import java.time.LocalDate;
//
//@Entity
//@NoArgsConstructor
//@Getter
//@Setter
//public class Analysis extends Auditable { // 성과분석
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "analysis_pk")
//    private Long pk; // 식별자
//
//    // 고객 유형별
//    @Column
//    private Double TARatio; // 이번달 히스토리 TA 비율
//
//    @Column
//    private Double APRatio; // 이번달 히스토리 AP 비율
//
//    @Column
//    private Double PCRatio; // 이번달 히스토리 PC 비율
//
//    @Column
//    private Long TA; // 이번달 히스토리 TA 개수
//
//    @Column
//    private Long AP; // 이번달 히스토리 AP 개수
//
//    @Column
//    private Long PC; // 이번달 히스토리 PC 개수
//
//    @Column
//    private int subscriptionCount; // 이번달 청약 개수
//
//    // 모든 고객별(AD+OD+CD+CP+JD)
//    @Column
//    private Double allTARatio; // 고객 all(AD+OD+CD+CP+JD)에 관한 이번달 히스토리 TA 비율
//
//    @Column
//    private Double allAPRatio; // 고객 all(AD+OD+CD+CP+JD)에 관한 이번달 히스토리 AP 비율
//
//    @Column
//    private Double allPCRatio; // 고객 all(AD+OD+CD+CP+JD)에 관한 이번달 히스토리 PC 비율
//
//    @Column
//    private Double allHistoryRatio; // 이번달 분배만 받고 히스토리 아예없는 Db개수 /전체 이번달Db분배 받은 개수
//
//    @Column
//    private LocalDate date; // 성과분석(년-월-01)
//
//    @Enumerated(EnumType.STRING)
//    private CustomerTType customerType;
//
//    @ManyToOne
//    @JoinColumn(name = "employee_pk")
//    private Employee employee;
//}