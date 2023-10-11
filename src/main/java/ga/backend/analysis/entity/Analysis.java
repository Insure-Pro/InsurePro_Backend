package ga.backend.analysis.entity;

import ga.backend.auditable.Auditable;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.employee.entity.Employee;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private Double allTAPercentage; // 전체 대비 TA 확률

    @Column
    private Double allAPPercentage; // 전체 대비 AP 확률

    @Column
    private Double allPCPercentage; // 전체 대비 PC 확률

    @Column
    private Double TAPercentage; // TA 진행확률

    @Column
    private Double APPercentage; // AP 진행확률

    @Column
    private Double PCPercentage; // PC 진행확률

    @Column
    private Double allTARatio; // 전체 히스토리 TA 비율

    @Column
    private Double allAPRatio; // 전체 히스토리 AP 비율

    @Column
    private Double allPCRatio; // 전체 히스토리 PC 비율

    @Column
    private Double subscriptionPercentage; // 청약확률

    @Column
    private LocalDate date; // 성과분석(년-월-01)

    @OneToOne
    @JoinColumn(name = "employee_pk")
    private Employee employee;
}