package ga.backend.gu2.entity;

import ga.backend.auditable.Auditable;
import ga.backend.customer.entity.Customer;
import ga.backend.dong2.entity.Dong2;
import ga.backend.metro2.entity.Metro2;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Gu2 extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gu2_pk")
    private Long pk;

    @OneToMany(mappedBy = "gu2", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Dong2> dongs = new ArrayList<>();

    @OneToMany(mappedBy = "gu2", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Customer> customers;

    @ManyToOne
    @JoinColumn(name = "metro2_pk")
    private Metro2 metro2;

    @Column
    private String guName;

    @Column
    private Double x;

    @Column
    private Double y;

    @Column
    private Boolean delYn = false;
}