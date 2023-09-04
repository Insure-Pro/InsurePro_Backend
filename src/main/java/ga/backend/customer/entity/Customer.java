package ga.backend.customer.entity;

import ga.backend.auditable.Auditable;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.dong.entity.Dong;
import ga.backend.employee.entity.Employee;
import ga.backend.li.entity.Li;
import ga.backend.schedule.entity.Schedule;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Customer extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_pk")
    private Long pk;

    @OneToOne
    @JoinColumn(name = "li_pk")
    private Li li;

    @ManyToOne
    @JoinColumn(name = "customer_type_pk")
    private CustomerType customerType;

    @ManyToOne
    @JoinColumn(name = "employee_pk")
    private Employee employee;

    @OneToMany(mappedBy = "customer", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Schedule> schedules = new ArrayList<>();

    @Column
    private String name; // 이름

    @Column
    private LocalDate birth; // 생년월일

    @Column
    private int age; // 나이

    @Column
    private String dongString; // 행정동 주소

    @Column
    private String address; // 상세 주소

    @Column
    private String phone; // 연락처

    @Column
    private String memo; // 메모

    @Column
    private Boolean contractYn = false; // 계약 체결 여부

    @Column
    private Boolean delYn = false; // 고객 삭제 여부

    @Column
    private LocalDate intensiveCareStartDate; // 집중관리시기 - 시작

    @Column
    private LocalDate intensiveCareFinishDate; // 집중관리시기 - 끝

    @Column
    private LocalDate registerDate; // 고객 등록 날짜

}