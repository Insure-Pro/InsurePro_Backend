package ga.backend.customer.mapper;

import ga.backend.customer.dto.CustomerRequestDto;
import ga.backend.customer.dto.CustomerResponseDto;
import ga.backend.customer.entity.Customer;
import ga.backend.dong2.entity.Dong2;
import ga.backend.gu2.entity.Gu2;
import ga.backend.metro2.entity.Metro2;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer customerPostDtoToCustomer(CustomerRequestDto.Post post);
    List<Customer> customersPostDtoToCustomers(List<CustomerRequestDto.Post> posts);
    default List<CustomerRequestDto.MetroGuDong> customersPostDtoToCustomersPostMetroGuDong(List<CustomerRequestDto.Post> posts) {
        return posts.stream().map(CustomerRequestDto.Post::getMetroGuDong).collect(Collectors.toList());
    }

    Customer customerPatchDtoToCustomer(CustomerRequestDto.Patch patch);

    CustomerResponseDto.Response customerToCustomerResponseDto(Customer customer);
    List<CustomerResponseDto.Response> customersToCustomerResponseDtos(List<Customer> customer);

    List<CustomerResponseDto.Response> customersToCustomersResponseDto(List<Customer> customers);

    default CustomerResponseDto.MetroGuDong customerToCustomerResponse(Customer customer) {
        CustomerResponseDto.MetroGuDong metroGuDong = new CustomerResponseDto.MetroGuDong();
        if(customer.getDongString() != null) {
            String[] dongString = customer.getDongString().split(" ");
            metroGuDong.setMetroName(dongString[0]);
            if (dongString.length >= 2) metroGuDong.setGuName(dongString[1]);
            if (dongString.length >= 3) metroGuDong.setDongName(dongString[2]);
        }

        return metroGuDong;
    }

    CustomerResponseDto.MetroGuDongResponse customerToCustomerResponseMetroGuDongDto(Customer customer);

    default CustomerResponseDto.MetroGuDongResponse customerToCustomerResponseMetroGuDongDtoCustom(Customer customer) {
        CustomerResponseDto.MetroGuDongResponse response = customerToCustomerResponseMetroGuDongDto(customer);
        response.setMetroGuDong(customerToCustomerResponse(customer));
        return response;
    }

    List<CustomerResponseDto.CoordinateResponse> customersToCustomerResponseCoordiateDto(List<Customer> customers);

    default List<CustomerResponseDto.CoordinateResponse> customersToCustomerResponseCoordiateDtoCustom(List<Customer> customers, List<Map<String, Double>> coordinates) {
        List<CustomerResponseDto.CoordinateResponse> responses = customersToCustomerResponseCoordiateDto(customers);

        for(int i=0; i<responses.size(); i++) {
            CustomerResponseDto.Coordinate coordinate = new CustomerResponseDto.Coordinate(
                    coordinates.get(i).get("x"),
                    coordinates.get(i).get("y")
            );
            responses.get(i).setCoordinate(coordinate);
        }

        return responses;
    }

}