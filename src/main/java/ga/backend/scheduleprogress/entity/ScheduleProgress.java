package ga.backend.scheduleprogress.entity;

import ga.backend.auditable.Auditable;
import ga.backend.customer.entity.Customer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class ScheduleProgress extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_progress_pk")
    private Long pk;

}