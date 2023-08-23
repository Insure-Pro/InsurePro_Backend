package ga.backend.customerType.entity;

import ga.backend.auditable.Auditable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class CustomerType extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @Column
    private String type; // 고객 유형

    @Column
    private String detail; // 설명

    @Column
    private boolean delYn = false; // 고객 유형 삭제 여부

}