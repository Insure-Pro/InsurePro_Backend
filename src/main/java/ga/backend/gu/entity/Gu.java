package ga.backend.gu.entity;

import ga.backend.metro.entity.Metro;
import ga.backend.auditable.Auditable;
import ga.backend.dong.entity.Dong;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Gu extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gu_pk")
    private Long pk;

    @OneToMany(mappedBy = "gu", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    List<Dong> dongs = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "metro_pk")
    Metro metro;

    @Column
    private String gu;

    @Column
    private Boolean delYn = false;
}