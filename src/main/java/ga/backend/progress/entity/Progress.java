package ga.backend.progress.entity;

import ga.backend.auditable.Auditable;
import ga.backend.company.entity.Company;
import ga.backend.employee.entity.Employee;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Getter
@Setter
public class Progress extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_pk")
    private Long pk;

    @Column
    private String optionName;

    @Column
    private boolean delYn = false;

    @ManyToOne
    @JoinColumn(name = "employee_pk")
    private Employee employee;
}