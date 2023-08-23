package ga.backend.schedule.entity;

import ga.backend.auditable.Auditable;
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
    private Long pk;
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
    private boolean meetYn = false;
    @Column
    private boolean delYn = false;
}