package ga.backend.company.entity;

import ga.backend.auditable.Auditable;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.employee.entity.Employee;
import ga.backend.performance.entity.Performance;
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
public class Company extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_pk")
    private Long pk;

    @Column
    private String name; // 회사명

    @Column
    private boolean delYn = false; // 회사 삭제 여부

    @OneToMany(mappedBy = "company", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Employee> employees = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<CustomerType> customerTypes = new ArrayList<>();

}