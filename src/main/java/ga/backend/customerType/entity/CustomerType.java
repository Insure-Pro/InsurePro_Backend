package ga.backend.customerType.entity;

import ga.backend.analysis.entity.Analysis;
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
@Table(name = "customer_type", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "company_pk"}) // 중복된 name이어도, 회사가 다르면 생성될 수 있도록 함
})
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

    @OneToMany(mappedBy = "customerType", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Analysis> analysises;

    @Column
    private Long employeePk; // 관여한 사람

    @Column
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

    @Column
    private Integer asSetting; // AS 가능한 TA 횟수
}