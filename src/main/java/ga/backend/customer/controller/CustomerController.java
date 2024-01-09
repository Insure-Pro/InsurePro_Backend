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
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
        Customer customer = customerService.createCustomer(
                customerMapper.customerPostDtoToCustomer(post),
                post.getMetroGuDong()
        );

        CustomerResponseDto.Response response = customerMapper.customerToCustomerResponseDto(customer);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/customers")
    public ResponseEntity postCustomers(@Valid @RequestBody List<CustomerRequestDto.Post> posts) {
        List<Customer> customers = customerService.createCustomers(
                customerMapper.customersPostDtoToCustomers(posts)
        );

        List<CustomerResponseDto.Response> responses = customerMapper.customersToCustomerResponseDtos(customers);

        return new ResponseEntity<>(responses, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/customer/{customer-pk}")
    public ResponseEntity getCustomer(@Positive @PathVariable("customer-pk") long customerPk) {
        Customer customer = customerService.findCustomer(customerPk);
        CustomerResponseDto.MetroGuDongResponse response = customerMapper.customerToCustomerResponseMetroGuDongDtoCustom(customer);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 최신순 정렬 - 생성일 기준
    @GetMapping("/customers/latest")
    public ResponseEntity getCustomers() {
        List<Customer> customers = customerService.findCustomerByLatest();
        List<CustomerResponseDto.Response> responses = customerMapper.customersToCustomersResponseDto(customers);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 최신순 정렬 - 생성일 기준 (+ metroGuDong 추가)
    @GetMapping("/customers/latest-2")
    public ResponseEntity getCustomersAddMetro() {
        List<Customer> customers = customerService.findCustomerByLatest();
        List<Map<String, Double>> coordinates = customerService.findCoordinate(customers);
        List<CustomerResponseDto.CoordinateResponse> responses =
                customerMapper.customersToCustomerResponseCoordiateDtoCustom(customers, coordinates);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 월별 최신순 정렬 - 생성일 기준
    @GetMapping("/customers/latest/{date}")
    public ResponseEntity getCustomersByMonth(@PathVariable("date") String date) {
        List<Customer> customers = customerService.findCustomerByLatest(LocalDate.parse(date));
        List<CustomerResponseDto.Response> responses = customerMapper.customersToCustomersResponseDto(customers);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 나이별 정렬(1020, 3040, 5060, 7080)
    @GetMapping("/customers/age/{age}")
    public ResponseEntity getCustomersByAge(@PathVariable("age") String age) {
        List<Customer> customers = customerService.findCustomerByAge(age);
        List<CustomerResponseDto.Response> responses = customerMapper.customersToCustomersResponseDto(customers);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 월별 나이별 정렬(1020, 3040, 5060, 7080)
    @GetMapping("/customers/age/{age}/{date}")
    public ResponseEntity getCustomersByAgeAndMonth(@PathVariable("age") String age,
                                                    @PathVariable("date") String date) {
        List<Customer> customers = customerService.findCustomerByAge(age, LocalDate.parse(date));
        List<CustomerResponseDto.Response> responses = customerMapper.customersToCustomersResponseDto(customers);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 지역이름으로 정렬
//    @GetMapping("/customers/dongName")
//    public ResponseEntity findCustomersByDongName(@RequestParam("dongPk") long dongPk) {
//        List<Customer> customers = customerService.findCustomerByLi(dongPk);
//        List<CustomerResponseDto.Response> responses = customerMapper.customersToCustomersResponseDto(customers);
//
//        return new ResponseEntity<>(responses, HttpStatus.OK);
//    }

    // 지역별 정렬
    @GetMapping("/customers")
    public ResponseEntity findCustomersByDong(@RequestParam("dongPk") long dongPk) {
        List<Customer> customers = customerService.findCustomerByLi(dongPk);
        List<CustomerResponseDto.Response> responses = customerMapper.customersToCustomersResponseDto(customers);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 월별 지역별 정렬
    @GetMapping("/customers/{date}")
    public ResponseEntity findCustomersByDongAndMonth(@RequestParam("dongPk") long dongPk,
                                                      @PathVariable("date") String date) {
        List<Customer> customers = customerService.findCustomerByLi(dongPk, LocalDate.parse(date));
        List<CustomerResponseDto.Response> responses = customerMapper.customersToCustomersResponseDto(customers);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 계약여부 정렬(최신순)
    @GetMapping("/customers/contractYn/{contractYn}/latest")
    public ResponseEntity findCustomersByContractYnByLatest(@PathVariable("contractYn") boolean contractYn) {
        List<Customer> customers = customerService.findCustomerByContractYnByLatest(contractYn);
        List<CustomerResponseDto.Response> responses = customerMapper.customersToCustomersResponseDto(customers);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 계약여부 정렬(나이대별)
    @GetMapping("/customers/contractYn/{contractYn}/age/{age}")
    public ResponseEntity findCustomersByContractYnByAge(@PathVariable("contractYn") boolean contractYn,
                                                         @PathVariable("age") String age) {
        List<Customer> customers = customerService.findCustomerByContractYnByLatest(contractYn, age);
        List<CustomerResponseDto.Response> responses = customerMapper.customersToCustomersResponseDto(customers);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 월별 계약여부 정렬
    @GetMapping("/customers/contractYn/{contractYn}/{date}")
    public ResponseEntity findCustomersByContractYnAndMonth(@PathVariable("contractYn") boolean contractYn,
                                                            @PathVariable("date") String date) {
        List<Customer> customers = customerService.findCustomerByContractYn(contractYn, LocalDate.parse(date));
        List<CustomerResponseDto.Response> responses = customerMapper.customersToCustomersResponseDto(customers);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 고객 이름 검색
    @GetMapping("/customers/name")
    public ResponseEntity findCustomerByname(@RequestBody CustomerRequestDto.Name customerName) {
        List<Customer> customers = customerService.findCustomerByName(customerName.getName());
        List<CustomerResponseDto.Response> responses = customerMapper.customersToCustomersResponseDto(customers);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // UPDATE
    // 고객 수정(모든 값 수정 가능)
    @PatchMapping("/customer/{customer-pk}")
    public ResponseEntity patchCustomer(@Positive @PathVariable("customer-pk") long customerPk,
                                        @Valid @RequestBody CustomerRequestDto.Patch patch) {
        patch.setPk(customerPk);
        Customer customer = customerService.patchCustomer(
                customerMapper.customerPatchDtoToCustomer(patch),
                patch.getMetroGuDong()
        );
        CustomerResponseDto.Response response = customerMapper.customerToCustomerResponseDto(customer);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 고객 수정(contractYn 수정)
    @PatchMapping("/customer/{customer-pk}/contractYn")
    public ResponseEntity patchCustomerByContractYn(@Positive @PathVariable("customer-pk") long customerPk,
                                                    @Valid @RequestBody CustomerRequestDto.Patch patch) {
        patch.setPk(customerPk);
        Customer customer = customerService.patchCustomer(
                customerMapper.customerPatchDtoToCustomer(patch),
                patch.getMetroGuDong()
        );
        CustomerResponseDto.Response response = customerMapper.customerToCustomerResponseDto(customer);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
//    @DeleteMapping("/customer/{customer-pk}")
//    public ResponseEntity deleteCustomer(@Positive @PathVariable("customer-pk") long customerPk) {
//        customerService.deleteCustomer(customerPk);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

    @DeleteMapping("/customer/{customer-pk}")
    public ResponseEntity deleteCustomer(@Positive @PathVariable("customer-pk") long customerPk) {
        customerService.deleteCustomer(customerPk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
