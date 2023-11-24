package ga.backend.dong2.entity;

import ga.backend.auditable.Auditable;
import ga.backend.customer.entity.Customer;
import ga.backend.gu2.entity.Gu2;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Dong2 extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dong2_pk")
    private Long pk;

    @ManyToOne
    @JoinColumn(name = "gu2_pk")
    private Gu2 gu2;

    @OneToMany(mappedBy = "dong2", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Customer> customers;

    @Column
    private String dongName;

    @Column
    private Boolean delYn = false;
}