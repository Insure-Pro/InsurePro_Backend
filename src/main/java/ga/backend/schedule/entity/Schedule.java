package ga.backend.schedule.entity;

import ga.backend.auditable.Auditable;
import ga.backend.customer.entity.Customer;
import ga.backend.employee.entity.Employee;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    @Getter
    public static enum Progress {
        TA("TA"),
        AP("AP"),
        PC("PC"),
        PT("PT");

        private final String value;

        Progress(String value) {
            this.value = value;
        }
    }

    @Column
    private Boolean meetYn = false;

    @Column
    private Boolean delYn = false;
}