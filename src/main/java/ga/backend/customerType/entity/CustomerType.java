package ga.backend.customerType.entity;

import ga.backend.auditable.Auditable;
import ga.backend.company.entity.Company;
import ga.backend.customer.entity.Customer;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class CustomerType extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_type_pk")
    private Long pk;

    @Column
    private String type;

    @Column
    private String detail;

    @Column
    private boolean delYn = false;

    @ManyToOne
    @JoinColumn(name = "company_pk")
    private Company company;

    @OneToMany(mappedBy = "customerType")
    private List<Customer> customers;
}