package ga.backend.photo.entity;

import ga.backend.auditable.Auditable;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.employee.entity.Employee;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Photo extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "photo_pk")
    private Long pk;

    @Column
    private String name; // 사진 이름

    @Column
    private String photoUrl; // 사진 경로

    @ManyToOne
    @JoinColumn(name = "employee_pk")
    private Employee employee;

    @Column
    private boolean delYn = false; // photo 삭제 여부

}