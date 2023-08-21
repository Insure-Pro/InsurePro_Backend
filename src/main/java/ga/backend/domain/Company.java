package ga.backend.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "tbl_company")
@Getter
@Setter
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long pk;
    @Column
    public String name;
    @Column
    public String delYn;
}
