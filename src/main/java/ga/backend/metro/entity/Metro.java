package ga.backend.metro.entity;

import ga.backend.auditable.Auditable;
import ga.backend.gu.entity.Gu;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Metro extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metro_pk")
    private Long pk;

    @OneToMany(mappedBy = "metro", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    List<Gu> gus = new ArrayList<>();

    @Column
    private String metro;

    @Column
    private Boolean delYn = false;
}