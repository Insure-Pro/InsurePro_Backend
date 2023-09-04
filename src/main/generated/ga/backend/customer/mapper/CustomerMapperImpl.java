package ga.backend.customer.mapper;

import ga.backend.customer.dto.CustomerRequestDto;
import ga.backend.customer.dto.CustomerResponseDto;
import ga.backend.customer.entity.Customer;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-04T21:03:21+0900",
    comments = "version: 1.5.1.Final, compiler: javac, environment: Java 11.0.16.1 (Oracle Corporation)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public Customer customerPostDtoToCustomer(CustomerRequestDto.Post post) {
        if ( post == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setPk( post.getPk() );
        customer.setName( post.getName() );
        customer.setBirth( post.getBirth() );
        customer.setAge( post.getAge() );
        customer.setAddress( post.getAddress() );
        customer.setPhone( post.getPhone() );
        customer.setMemo( post.getMemo() );
        customer.setContractYn( post.isContractYn() );
        customer.setDelYn( post.isDelYn() );

        return customer;
    }

    @Override
    public Customer customerPatchDtoToCustomer(CustomerRequestDto.Patch patch) {
        if ( patch == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setPk( patch.getPk() );
        customer.setName( patch.getName() );
        customer.setBirth( patch.getBirth() );
        customer.setAge( patch.getAge() );
        customer.setAddress( patch.getAddress() );
        customer.setPhone( patch.getPhone() );
        customer.setMemo( patch.getMemo() );
        customer.setContractYn( patch.isContractYn() );
        customer.setDelYn( patch.isDelYn() );

        return customer;
    }

    @Override
    public CustomerResponseDto.Response customerToCustomerResponseDto(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        Long pk = null;
        String name = null;
        String birth = null;
        int age = 0;
        String address = null;
        String phone = null;
        String memo = null;
        boolean contractYn = false;
        boolean delYn = false;
        LocalDateTime createdAt = null;
        LocalDateTime modifiedAt = null;

        pk = customer.getPk();
        name = customer.getName();
        birth = customer.getBirth();
        age = customer.getAge();
        address = customer.getAddress();
        phone = customer.getPhone();
        memo = customer.getMemo();
        contractYn = customer.isContractYn();
        delYn = customer.isDelYn();
        createdAt = customer.getCreatedAt();
        modifiedAt = customer.getModifiedAt();

        CustomerResponseDto.Response response = new CustomerResponseDto.Response( pk, name, birth, age, address, phone, memo, contractYn, delYn, createdAt, modifiedAt );

        return response;
    }
}
