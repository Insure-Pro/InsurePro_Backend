package ga.backend.progress.entity;

import ga.backend.auditable.Auditable;
import ga.backend.company.entity.Company;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Table(name="tbl_progress")
@Getter
@Setter
public class Progress extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @Column
    private String option;

    @Column
    private boolean delYn = false;
}
