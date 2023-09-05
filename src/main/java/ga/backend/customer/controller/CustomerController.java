package ga.backend.customer.controller;

import ga.backend.customer.dto.CustomerRequestDto;
import ga.backend.customer.dto.CustomerResponseDto;
import ga.backend.customer.entity.Customer;
import ga.backend.customer.mapper.CustomerMapper;
import ga.backend.customer.service.*;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.customerType.service.CustomerTypeService;
import ga.backend.util.Version;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(Version.currentUrl)
@Validated
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    // CREATE
    @PostMapping("/customer")
    public ResponseEntity postCustomer(@Valid @RequestBody CustomerRequestDto.Post post) {
        Customer customer = customerService.createCustomer(customerMapper.customerPostDtoToCustomer(post),
                post.getCustomerTypePk(),
                post.getLiPk())
                ;
        CustomerResponseDto.Response response = customerMapper.customerToCustomerResponseDto(
                customer
                ,
                customer.getCustomerType().getType()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/customer/{customer-pk}")
    public ResponseEntity getCustomer(@Positive @PathVariable("customer-pk") long customerPk) {
        Customer customer = customerService.findCustomer(customerPk);
        CustomerResponseDto.Response response = customerMapper.customerToCustomerResponseDto(customer, customer.getCustomerType().getType());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 최신순 정렬 - 생성일 기준
    @GetMapping("/customers/latest")
    public ResponseEntity getCustomers() {
        List<Customer> customers = customerService.findCustomerByLatest();
        List<CustomerResponseDto.Response> responses = customerMapper.customerToCustomerResponseDto(customers);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 나이별 정렬(2030, 4050, 6070)
    @GetMapping("/customers/age/{age}")
    public ResponseEntity getCustomers(@PathVariable("age") String age) {
        List<Customer> customers = customerService.findCustomerByAge(age);
        List<CustomerResponseDto.Response> responses = customerMapper.customerToCustomerResponseDto(customers);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // UPDATE
    @PatchMapping("/customer/{customer-pk}")
    public ResponseEntity patchCustomer(@Positive @PathVariable("customer-pk") long customerPk,
                                        @Valid @RequestBody CustomerRequestDto.Patch patch) {
        patch.setPk(customerPk);
        Customer customer = customerService.patchCustomer(customerMapper.customerPatchDtoToCustomer(patch));
        CustomerResponseDto.Response response = customerMapper.customerToCustomerResponseDto(customer, customer.getCustomerType().getType());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/customer/{customer-pk}")
    public ResponseEntity deleteCustomer(@Positive @PathVariable("customer-pk") long customerPk) {
        customerService.deleteCustomer(customerPk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
