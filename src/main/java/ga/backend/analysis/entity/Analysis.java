package ga.backend.analysis.entity;

import ga.backend.auditable.Auditable;
import ga.backend.employee.entity.Employee;
import ga.backend.util.CustomerType;
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

    @Column
    private Double TARatio; // 이번달 히스토리 TA 비율

    @Column
    private Double APRatio; // 이번달 히스토리 AP 비율

    @Column
    private Double PCRatio; // 이번달 히스토리 PC 비율

    @Column
    private int subscriptionCount; // 이범달 청약 개수

    @Column
    private LocalDate date; // 성과분석(년-월-01)

    @Enumerated(EnumType.STRING)
    private CustomerType customerType;

    @ManyToOne
    @JoinColumn(name = "employee_pk")
    private Employee employee;
}