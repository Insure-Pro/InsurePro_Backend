package ga.backend.customerType.entity;

import ga.backend.auditable.Auditable;
import ga.backend.company.entity.Company;
import ga.backend.customer.entity.Customer;
import ga.backend.hide.entity.Hide;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class CustomerType extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_type_pk")
    private Long pk;

    @ManyToOne
    @JoinColumn(name = "company_pk")
    private Company company;

    @OneToMany(mappedBy = "customerType", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Customer> customers;

    @OneToMany(mappedBy = "customerType", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Hide> hides = new ArrayList<>();

    @Column
    private Long employeePk; // 관여한 사람

    @Column(unique = true)
    private String name; // 고객유형 이름

    @Column
    private String color; // 색상

    @Column
    private String detail; // 상세내용

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DataType dataType; // DB 유형

    @Column
    private Boolean delYn = false; // 고객 삭제 여부
}