package ga.backend.schedule.entity;

import ga.backend.auditable.Auditable;
import ga.backend.contract.entity.Contract;
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
public class Schedule extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="schedule_pk")
    private Long pk;

    @ManyToOne
    @JoinColumn(name = "customer_pk")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "employee_pk")
    private Employee employee;

    @OneToMany(mappedBy = "schedule", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Contract> contracts = new ArrayList<>();

    @Column
    private String memo;

    @Column
    private LocalDate date;

    @Column
    private LocalTime startTm;

    @Column
    private LocalTime finishTm;

    @Column
    private LocalTime time;

    @Column
    private String address;

    @Column
    private String color;

    @Enumerated(EnumType.STRING)
    private Progress progress;

    @Column
    private Boolean meetYn = false;

    @Column(nullable = false)
    private Boolean delYn = false;
}