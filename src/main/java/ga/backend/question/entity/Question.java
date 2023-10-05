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

    private String content;

    @ManyToOne
    @JoinColumn(name = "employee_pk")
    private Employee employee;
}