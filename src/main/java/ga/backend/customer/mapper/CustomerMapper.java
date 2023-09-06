package ga.backend.customer.mapper;

import ga.backend.customer.dto.CustomerRequestDto;
import ga.backend.customer.dto.CustomerResponseDto;
import ga.backend.customer.entity.Customer;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer customerPostDtoToCustomer(CustomerRequestDto.Post post);
    Customer customerPatchDtoToCustomer(CustomerRequestDto.Patch patch);
    CustomerResponseDto.Response customerToCustomerResponseDto(Customer customer, String customerTypeString);
    default List<CustomerResponseDto.Response> customerToCustomerResponseDto(List<Customer> customers) {
        List<CustomerResponseDto.Response> responses = new ArrayList<>();

        for (Customer customer:customers) {
            responses.add(this.customerToCustomerResponseDto(customer, customer.getCustomerType().getType()));
        }

        return responses;
    }
}