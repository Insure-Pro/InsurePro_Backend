package ga.backend.util;

import ga.backend.customer.mapper.CustomerMapper;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.customerType.entity.DataType;
import ga.backend.customerType.repository.CustomerTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class InitialCustomerType {
    private final CustomerTypeRepository customerTypeRepository;

    public InitialCustomerType(CustomerTypeRepository customerTypeRepository) {
        this.customerTypeRepository = customerTypeRepository;

        // customerType 기본값(NULL)이 없을 경우 자동으로 생성하기
        Optional<CustomerType> optionalCustomerType = customerTypeRepository.findByName("NULL");
        if(optionalCustomerType.isEmpty()) {
            CustomerType customerType = new CustomerType();
            customerType.setName("NULL");
            customerType.setPk(999L);
            customerType.setDetail("customerType을 지정하지 않을 경우 사용하는 default값");
            customerType.setDataType(DataType.ETC);
            customerTypeRepository.save(customerType);
        }
    }
}
