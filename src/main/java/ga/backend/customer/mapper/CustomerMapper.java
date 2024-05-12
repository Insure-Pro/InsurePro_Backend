package ga.backend.customer.mapper;

import ga.backend.customer.dto.CustomerRequestDto;
import ga.backend.customer.dto.CustomerResponseDto;
import ga.backend.customer.entity.Customer;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.dong2.entity.Dong2;
import ga.backend.gu2.entity.Gu2;
import ga.backend.metro2.entity.Metro2;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer customerPostDtoToCustomer(CustomerRequestDto.Post post);

    default List<Customer> customersPostDtoToCustomers(List<CustomerRequestDto.Post> posts) {
        if (posts == null) {
            return null;
        }

        List<Customer> list = new ArrayList<>(posts.size());
        for (CustomerRequestDto.Post post : posts) {
            Customer customer = customerPostDtoToCustomer(post);
            CustomerType customerType = new CustomerType();
            customerType.setPk(post.getCustomerTypePk());
            customer.setCustomerType(customerType);
            list.add(customer);
        }

        return list;
    }

    Customer customerPatchDtoToCustomer(CustomerRequestDto.Patch patch);

    CustomerResponseDto.Response customerToCustomerResponseDto(Customer customer);

    List<CustomerResponseDto.Response> customersToCustomersResponseDto(List<Customer> customers);

    // metro, gu, dong response
    default CustomerResponseDto.MetroGuDong customerToCustomerResponse(Customer customer) {
        // metro, gu, dong 값 설정
        CustomerResponseDto.MetroGuDong metroGuDong = new CustomerResponseDto.MetroGuDong();
        if (customer.getDongString() != null) {
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

    default List<CustomerResponseDto.MetroGuDongResponse> customersToCustomerResponseMetroGuDongDto(List<Customer> customers) {
        return customers.stream().map(this::customerToCustomerResponseMetroGuDongDtoCustom).collect(Collectors.toList());
    }

    // coordinate response
    List<CustomerResponseDto.CoordinateResponse> customersToCustomerResponseCoordiateDto(List<Customer> customers);

    default List<CustomerResponseDto.CoordinateResponse> customersToCustomerResponseCoordiateDtoCustom(List<Customer> customers, List<Map<String, Double>> coordinates) {
        List<CustomerResponseDto.CoordinateResponse> responses = customersToCustomerResponseCoordiateDto(customers);

        // 주소 좌표값 설정
        for (int i = 0; i < responses.size(); i++) {
            CustomerResponseDto.Coordinate coordinate = new CustomerResponseDto.Coordinate(
                    coordinates.get(i).get("x"),
                    coordinates.get(i).get("y")
            );
            responses.get(i).setCoordinate(coordinate);
        }

        return responses;
    }

}