package ga.backend.customer.entity;

import ga.backend.auditable.Auditable;
import ga.backend.employee.Employee;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tbl_customer")
@Getter
@Setter
public class Customer extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @Column
    private String name; // 이름

    @Column
    private String birth; // 생년월일

    @Column
    private int age; // 나이

    @Column
    private String address; // 주소

    @Column
    private String phone; // 연락처

    @Column
    private String memo; // 메모

    @Column
    private boolean contractYn; // 계약 체결 여부

    @Column
    private boolean delYn; // 고객 삭제 여부
}