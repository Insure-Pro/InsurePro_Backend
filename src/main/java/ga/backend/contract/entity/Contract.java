package ga.backend.contract.entity;

import ga.backend.auditable.Auditable;
import ga.backend.customer.entity.Customer;
import ga.backend.employee.entity.Employee;
import ga.backend.schedule.entity.Schedule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Contract extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_pk")
    private Long pk;

    @ManyToOne
    @JoinColumn(name = "customer_pk")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "schedule_pk")
    private Schedule schedule;

    @Column
    private String name; // 보험 상품 이름

    @Column
    private String memo; // 메모

    @Column
    private LocalDate contractDate; // 계약 체결 날짜
}