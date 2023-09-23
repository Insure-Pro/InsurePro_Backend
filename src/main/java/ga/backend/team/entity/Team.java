package ga.backend.team.entity;

import ga.backend.auditable.Auditable;
import ga.backend.customer.entity.Customer;
import ga.backend.employee.entity.Employee;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Team extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="team_pk")
    private Long pk;

    private String teamName;

    private boolean delYn = false;

    @OneToMany(mappedBy = "team", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Employee> employees = new ArrayList<>();
}