package ga.backend.authorizationNumber.entity;

import ga.backend.auditable.Auditable;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.employee.entity.Employee;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class AuthorizationNumber extends Auditable {
    @Id
    @Email
    private String email;

    @Column
    private int authNum; // 인증번호
}