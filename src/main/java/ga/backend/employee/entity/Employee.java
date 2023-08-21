package ga.backend.employee.entity;

import ga.backend.auditable.Auditable;
import ga.backend.company.entity.Company;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.OffsetDateTime;

@Entity
@Table(name="tbl_employee")
@Getter
@Setter
public class Employee extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @Column
    private String id;

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
    @JoinColumn(name = "COMPANY_PK")
    private Company companyPk;
}
