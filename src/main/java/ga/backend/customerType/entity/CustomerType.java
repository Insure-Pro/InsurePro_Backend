package ga.backend.customerType.entity;

import ga.backend.auditable.Auditable;
import ga.backend.company.entity.Company;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Getter
@Setter
public class CustomerType extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "customer_type_pk")
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
}
