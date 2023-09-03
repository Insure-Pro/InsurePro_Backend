package ga.backend.li.entity;

import ga.backend.auditable.Auditable;
import ga.backend.customer.entity.Customer;
import ga.backend.dong.entity.Dong;
import ga.backend.gu.entity.Gu;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Li extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "li_pk")
    private Long pk;

    @OneToOne(mappedBy = "li", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "dong_pk")
    Dong dong;

    @Column
    private String li; // 리 이름

    @Column
    private double latitude; // 위도

    @Column
    private double longitude; // 경도

    @Column
    private Boolean delYn = false; // 삭제 여부
}