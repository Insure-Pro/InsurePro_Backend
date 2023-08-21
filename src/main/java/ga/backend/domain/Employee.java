package ga.backend.domain;

import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name="tbl_employee")
@Getter
@Setter
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;
    @Column
    private String id;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private OffsetDateTime regiDate;
    @Column
    private boolean regiYn;
    @Column
    private boolean delYn;
    @ManyToOne
    private Company companyPk;
}
