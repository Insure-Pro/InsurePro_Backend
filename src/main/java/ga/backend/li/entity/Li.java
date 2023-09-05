package ga.backend.li.entity;

import ga.backend.auditable.Auditable;
import ga.backend.customer.entity.Customer;
import ga.backend.dong.entity.Dong;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Li extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "li_pk")
    private Long pk;

    @OneToMany(mappedBy = "li", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Customer> customers;

    @ManyToOne
    @JoinColumn(name = "dong_pk")
    Dong dong;

    @Column
    private String liName; // 리 이름

    @Column
    private double latitude; // 위도

    @Column
    private double longitude; // 경도

    @Column
    private Boolean delYn = false; // 삭제 여부
}