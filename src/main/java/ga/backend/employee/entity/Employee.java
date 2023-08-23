package ga.backend.employee.entity;

import ga.backend.auditable.Auditable;
import ga.backend.company.entity.Company;
import ga.backend.performance.entity.Performance;
import ga.backend.progress.entity.Progress;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Employee extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_pk")
    private Long pk;

    @Column
    private String loginId;

    @Column
    @Email
    private String email;

    @Column
    private String password;

    @Column
    private boolean regiYn;

    @Column
    private boolean delYn;

    @ManyToOne
    @JoinColumn(name = "company_pk")
    private Company company;

    @OneToMany(mappedBy = "employee", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Performance> performances = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Progress> progresses = new ArrayList<>();
}