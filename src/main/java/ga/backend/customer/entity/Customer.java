package ga.backend.customer.entity;

import ga.backend.auditable.Auditable;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.dong2.entity.Dong2;
import ga.backend.employee.entity.Employee;
import ga.backend.gu2.entity.Gu2;
import ga.backend.li.entity.Li;
import ga.backend.metro2.entity.Metro2;
import ga.backend.schedule.entity.Schedule;
import ga.backend.ta.entity.TA;
import ga.backend.util.ConsultationStatus;
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

    @ManyToOne
    @JoinColumn(name = "li_pk")
    private Li li;

    @ManyToOne
    @JoinColumn(name = "metro2_pk")
    private Metro2 metro2;

    @ManyToOne
    @JoinColumn(name = "gu2_pk")
    private Gu2 gu2;

    @ManyToOne
    @JoinColumn(name = "dong2_pk")
    private Dong2 dong2;

    @ManyToOne
    @JoinColumn(name = "employee_pk")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "customer_type_pk")
    private CustomerType customerType;

    @OneToMany(mappedBy = "customer", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<TA> tas = new ArrayList<>();

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
    private String state; // 인수상태

    @Column
    private Boolean contractYn = false; // 계약 체결 여부

    @Column
    private Boolean delYn = false; // 고객 삭제 여부

    @Column
    private LocalDate registerDate; // 고객 등록 날짜

    @Column
    @Enumerated(EnumType.STRING)
    private ConsultationStatus consultationStatus; // 상담현황(상담보류중, 상품제안중, 병력대기, 청약거절, 상담거절, AS대상)
}