package ga.backend.dong.entity;

import ga.backend.auditable.Auditable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tbl_dong")
@Getter
@Setter
public class Dong extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @Column
    private String si;
    @Column
    private String gu;
    @Column
    private String dong;
    @Column
    private boolean delYn;
}