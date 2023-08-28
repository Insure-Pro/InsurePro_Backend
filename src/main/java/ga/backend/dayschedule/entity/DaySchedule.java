package ga.backend.dayschedule.entity;

import ga.backend.auditable.Auditable;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.employee.entity.Employee;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class DaySchedule extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "day_schedule_pk")
    private Long pk;

    @ManyToOne
    @JoinColumn(name = "employee_pk")
    private Employee employee;

    @Column
    private String content; // 일정 내용

    @Column
    private boolean doYn; // 달성 여부

}