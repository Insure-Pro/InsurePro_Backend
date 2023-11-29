package ga.backend.metro2.entity;

import ga.backend.auditable.Auditable;
import ga.backend.customer.entity.Customer;
import ga.backend.gu2.entity.Gu2;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Metro2 extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metro2_pk")
    private Long pk;

    @OneToMany(mappedBy = "metro2", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    List<Gu2> gus = new ArrayList<>();

    @OneToMany(mappedBy = "metro2", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Customer> customers;

    @Column
    private String metroName;

    @Column
    private Long x;

    @Column
    private Long y;

    @Column
    private Boolean delYn = false;
}