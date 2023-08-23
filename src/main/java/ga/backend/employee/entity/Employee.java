package ga.backend.employee.entity;

import ga.backend.auditable.Auditable;
import ga.backend.company.entity.Company;
import ga.backend.performance.entity.Performance;
import ga.backend.progress.entity.Progress;
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

    @Column
    @Email
    private String email; // 이메일

    @Column
    private String password; // 비밀번호

    @Column
    private boolean regiYn = false; // 가입 승인 여부

    @Column
    private boolean delYn = false; // 직원 삭제 여부

    @Column
    private String accessToken; // access 토큰

    @Column
    private String refreshToken; // refresh 토큰

    @ManyToOne
    @JoinColumn(name = "company_pk")
    private Company company;

    @OneToMany(mappedBy = "employee", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Performance> performances = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Progress> progresses = new ArrayList<>();
}