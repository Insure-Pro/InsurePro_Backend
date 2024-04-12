package ga.backend.util;

import ga.backend.customerType.entity.CustomerType;
import ga.backend.customerType.entity.DataType;
import ga.backend.customerType.repository.CustomerTypeRepository;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class InitialCustomerType {
    private final CustomerTypeRepository customerTypeRepository;
    public static final Long NULL_CUSTOMERTYPE_PK = 999L;
    public static CustomerType CUSTOMER_TYPE_NULL = null;

    public InitialCustomerType(CustomerTypeRepository customerTypeRepository) {
        this.customerTypeRepository = customerTypeRepository;

        // customerType 기본값(NULL)이 없을 경우 자동으로 생성하기
        Optional<CustomerType> optionalCustomerType = customerTypeRepository.findByNameAndDelYnFalse("NULL");
        if(optionalCustomerType.isEmpty()) {
            CustomerType customerType = new CustomerType();
            customerType.setName("NULL");
            customerType.setPk(NULL_CUSTOMERTYPE_PK);
            customerType.setDetail("customerType을 지정하지 않을 경우 사용하는 default값");
            customerType.setDataType(DataType.ETC);
            customerType.setColor("#000000");
            CUSTOMER_TYPE_NULL = customerTypeRepository.save(customerType);
        } else
            CUSTOMER_TYPE_NULL = customerTypeRepository.save(optionalCustomerType.get());
    }
}
