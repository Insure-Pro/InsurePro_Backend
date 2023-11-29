package ga.backend.customer.mapper;

import ga.backend.customer.dto.CustomerRequestDto;
import ga.backend.customer.dto.CustomerResponseDto;
import ga.backend.customer.entity.Customer;
import ga.backend.dong2.entity.Dong2;
import ga.backend.gu2.entity.Gu2;
import ga.backend.metro2.entity.Metro2;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer customerPostDtoToCustomer(CustomerRequestDto.Post post);
    Customer customerPatchDtoToCustomer(CustomerRequestDto.Patch patch);
    CustomerResponseDto.Response customerToCustomerResponseDto(Customer customer);
    List<CustomerResponseDto.Response> customersToCustomersResponseDto(List<Customer> customers);

    CustomerResponseDto.Metro metroToCustomerResponseMetroGuDongDto(Metro2 metro2);
    CustomerResponseDto.Gu guToCustomerResponseMetroGuDongDto(Gu2 gu2);
    CustomerResponseDto.Dong dongToCustomerResponseMetroGuDongDto(Dong2 dong2);
    default CustomerResponseDto.MetroGuDong customerToCustomerResponse(Customer customer) {
        CustomerResponseDto.MetroGuDong metroGuDong = new CustomerResponseDto.MetroGuDong();
        metroGuDong.setMetro(metroToCustomerResponseMetroGuDongDto(customer.getMetro2()));
        metroGuDong.setGu(guToCustomerResponseMetroGuDongDto(customer.getGu2()));
        metroGuDong.setDong(dongToCustomerResponseMetroGuDongDto(customer.getDong2()));

        return metroGuDong;
    };

    CustomerResponseDto.metroGuDongResponse customerToCustomerResponseMetroGuDongDto(Customer customer);
    default CustomerResponseDto.metroGuDongResponse customerToCustomerResponseMetroGuDongDtoCustom(Customer customer) {
        CustomerResponseDto.metroGuDongResponse response = customerToCustomerResponseMetroGuDongDto(customer);
        response.setMetroGuDong(customerToCustomerResponse(customer));

        return response;
    }

    default List<CustomerResponseDto.metroGuDongResponse> customersToCustomerResponseMetroGuDongDtoCustom(List<Customer> customers) {
        return customers
                .stream()
                .map(this::customerToCustomerResponseMetroGuDongDtoCustom)
                .collect(Collectors.toList());
    }

}