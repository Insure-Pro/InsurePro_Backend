package ga.backend.dong.entity;

import ga.backend.auditable.Auditable;
import ga.backend.customer.entity.Customer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Dong extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dong_pk")
    private Long pk;

    @OneToMany(mappedBy = "dong")
    private List<Customer> customers;

    @Column
    private String si;
    @Column
    private String gu;
    @Column
    private String dong;
    @Column
    private boolean delYn;
}