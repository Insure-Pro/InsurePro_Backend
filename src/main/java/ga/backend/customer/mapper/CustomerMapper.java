package ga.backend.customer.mapper;

import ga.backend.customer.dto.CustomerRequestDto;
import ga.backend.customer.dto.CustomerResponseDto;
import ga.backend.customer.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer customerPostDtoToCustomer(CustomerRequestDto.Post post);
    Customer customerPatchDtoToCustomer(CustomerRequestDto.Patch patch);
    CustomerResponseDto.Response customerToCustomerResponseDto(Customer customer);
}