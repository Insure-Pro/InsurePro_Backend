package ga.backend.performance.entity;

import ga.backend.auditable.Auditable;
import ga.backend.company.entity.Company;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="tbl_performace")
@Getter
@Setter
public class Performance extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
