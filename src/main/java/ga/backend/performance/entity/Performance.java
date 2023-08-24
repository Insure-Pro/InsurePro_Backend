package ga.backend.performance.entity;

import ga.backend.auditable.Auditable;
import ga.backend.employee.entity.Employee;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Performance extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "performance_pk")
    private Long pk;

    @Column
    private LocalDate date;

    @Column
    private LocalTime startTm;

    @Column
    private LocalTime finishTm;

    @Column
    private int ta;

    @Column
    private int introduction;

    @Column
    private int rp;

    @Column
    private int apc;

    @Column
    private int contractNm;

    @ManyToOne
    @JoinColumn(name = "employee_pk")
    private Employee employee;
}
