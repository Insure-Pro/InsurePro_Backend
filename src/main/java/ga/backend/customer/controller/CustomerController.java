package ga.backend.customer.controller;

import ga.backend.customer.dto.CustomerRequestDto;
import ga.backend.customer.dto.CustomerResponseDto;
import ga.backend.customer.entity.Customer;
import ga.backend.customer.mapper.CustomerMapper;
import ga.backend.customer.service.*;
import ga.backend.util.Version;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(Version.currentUrl + "/customer")
@Validated
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    // CREATE
    @PostMapping
    public ResponseEntity postCustomer(@Valid @RequestBody CustomerRequestDto.Post post) {
        Customer customer = customerService.createCustomer(customerMapper.customerPostDtoToCustomer(post));
        CustomerResponseDto.Response response = customerMapper.customerToCustomerResponseDto(customer);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/{customer-pk}")
    public ResponseEntity getCustomer(@Positive @PathVariable("customer-pk") long customerPk) {
        Customer customer = customerService.findCustomer(customerPk);
        CustomerResponseDto.Response response = customerMapper.customerToCustomerResponseDto(customer);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // UPDATE
    @PatchMapping("/{customer-pk}")
    public ResponseEntity patchCustomer(@Positive @PathVariable("customer-pk") long customerPk,
                                        @Valid @RequestBody CustomerRequestDto.Patch patch) {
        patch.setPk(customerPk);
        Customer customer = customerService.patchCustomer(customerMapper.customerPatchDtoToCustomer(patch));
        CustomerResponseDto.Response response = customerMapper.customerToCustomerResponseDto(customer);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/{customer-pk}")
    public ResponseEntity deleteCustomer(@Positive @PathVariable("customer-pk") long customerPk) {
        customerService.deleteCustomer(customerPk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
