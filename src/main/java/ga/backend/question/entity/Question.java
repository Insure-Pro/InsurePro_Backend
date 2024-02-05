package ga.backend.question.entity;

import ga.backend.auditable.Auditable;
import ga.backend.employee.entity.Employee;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Question extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="question_pk")
    private Long pk;

    @Column
    private String content; // 문의 내용

    @Column
    private String imageUrl; // 문의 사진 url

    @ManyToOne
    @JoinColumn(name = "employee_pk")
    private Employee employee;
}