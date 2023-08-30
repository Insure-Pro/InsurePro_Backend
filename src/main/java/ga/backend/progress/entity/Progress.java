package ga.backend.progress.entity;

import ga.backend.auditable.Auditable;
import ga.backend.employee.entity.Employee;
import ga.backend.scheduleprogress.entity.ScheduleProgress;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Progress extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_pk")
    private Long pk;

    @OneToOne(mappedBy = "progress", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private ScheduleProgress scheduleProgress;

    @Column
    private String optionName;

    @Column
    private boolean delYn = false;

    @ManyToOne
    @JoinColumn(name = "employee_pk")
    private Employee employee;
}