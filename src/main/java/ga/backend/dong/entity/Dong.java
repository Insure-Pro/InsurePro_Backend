package ga.backend.dong.entity;

import ga.backend.auditable.Auditable;
import ga.backend.gu.entity.Gu;
import ga.backend.li.entity.Li;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Dong extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dong_pk")
    private Long pk;

    @OneToMany(mappedBy = "dong", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Li> lis = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "gu_pk")
    Gu gu;

    @Column
    private String dongName;

    @Column
    private Boolean delYn = false;
}