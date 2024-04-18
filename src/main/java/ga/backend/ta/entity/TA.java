package ga.backend.ta.entity;

import ga.backend.auditable.Auditable;
import ga.backend.company.entity.Company;
import ga.backend.customer.entity.Customer;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.employee.entity.Employee;
import ga.backend.util.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class TA extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ta_pk")
    private Long pk;

    @ManyToOne
    @JoinColumn(name = "employee_pk")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "customer_pk")
    private Customer customer;

//    @Min(1)
//    @Max(20)
    @Column
    private Integer count = 1; // 전화 횟수(1 ~ 20)

    @Column
    private LocalTime time; // 전화한 시간

    @Column
    private LocalDate date; // 전화한 날짜

    @Column
    private String memo; // 메모

    @Enumerated(EnumType.STRING)
    private Status status; // 상태(부재, 거절, 확약, AS대상)

    @Column
    private Boolean delYn = false; // 삭제여부
}