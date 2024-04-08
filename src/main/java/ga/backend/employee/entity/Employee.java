package ga.backend.employee.entity;

//import ga.backend.analysis.entity.Analysis;
import ga.backend.auditable.Auditable;
import ga.backend.company.entity.Company;
import ga.backend.customer.entity.Customer;
import ga.backend.dayschedule.entity.DaySchedule;
import ga.backend.hide.entity.Hide;
import ga.backend.performance.entity.Performance;
import ga.backend.photo.entity.Photo;
import ga.backend.question.entity.Question;
import ga.backend.schedule.entity.Schedule;
import ga.backend.ta.entity.TA;
import ga.backend.team.entity.Team;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Employee extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_pk")
    private Long pk;

    @Column
    private String id; // 사번

    @Column(unique = true)
    @Email
    private String email; // 이메일

    @Column
    private String name; // 이름

    @Column
    private String password; // 비밀번호

    @Column
    private Boolean regiYn = false; // 가입 승인 여부

    @Column
    private Boolean delYn = false; // 직원 삭제 여부

    @Column
    private String accessToken = ""; // access 토큰

    @Column
    private String refreshToken = ""; // refresh 토큰

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>(); // 권한

    @ManyToOne
    @JoinColumn(name = "company_pk")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "team_pk")
    private Team team;

//    @OneToMany(mappedBy = "employee", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
//    private List<Analysis> analysises;

    @OneToMany(mappedBy = "employee", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Performance> performances = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Customer> customers = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<DaySchedule> daySchedules = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Photo> photos = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Hide> hides = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<TA> tas = new ArrayList<>();
}