package ga.backend.customer.controller;

import ga.backend.customer.dto.CustomerRequestDto;
import ga.backend.customer.dto.CustomerResponseDto;
import ga.backend.customer.entity.Customer;
import ga.backend.customer.mapper.CustomerMapper;
import ga.backend.customer.service.*;
import ga.backend.customer.entity.ConsultationStatus;
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
                post.getMetroGuDong(),
                post.getCustomerTypePk()
        );

        CustomerResponseDto.Response response = customerMapper.customerToCustomerResponseDto(customer);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 여러 명의 customer 생성
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
    public ResponseEntity getCustomers(@RequestParam("customerTypePk") long customerTypePk) {
        List<Customer> customers = customerService.findCustomerByLatest(customerTypePk);
        List<CustomerResponseDto.Response> responses = customerMapper.customersToCustomersResponseDto(customers);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 최신순 정렬 - 생성일 기준 (+ metroGuDong 추가)
    @GetMapping("/customers/latest-2")
    public ResponseEntity getCustomersAddMetro(@RequestParam("customerTypePk") long customerTypePk) {
        List<Customer> customers = customerService.findCustomerByLatest(customerTypePk);
        List<Map<String, Double>> coordinates = customerService.findCoordinate(customers);
        List<CustomerResponseDto.CoordinateResponse> responses =
                customerMapper.customersToCustomerResponseCoordiateDtoCustom(customers, coordinates);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 월별 최신순 정렬 - 생성일 기준
    @GetMapping("/customers/latest/{date}")
    public ResponseEntity getCustomersByMonth(@PathVariable("date") String date,
                                              @RequestParam("customerTypePk") long customerTypePk) {
        List<Customer> customers = customerService.findCustomerByLatest(LocalDate.parse(date), customerTypePk);
        List<CustomerResponseDto.Response> responses = customerMapper.customersToCustomersResponseDto(customers);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 나이별 정렬(10, 20, 30, 40, 50, 60, 70, 80)
    @GetMapping("/customers/age/{age}")
    public ResponseEntity getCustomersByAge(@PathVariable("age") String age,
                                            @RequestParam("customerTypePk") long customerTypePk) {
        List<Customer> customers = customerService.findCustomerByAge(age, customerTypePk);
        List<CustomerResponseDto.Response> responses = customerMapper.customersToCustomersResponseDto(customers);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 월별 나이별 정렬(10, 20, 30, 40, 50, 60, 70, 80)
    @GetMapping("/customers/age/{age}/{date}")
    public ResponseEntity getCustomersByAgeAndMonth(@PathVariable("age") String age,
                                                    @PathVariable("date") String date,
                                                    @RequestParam("customerTypePk") long customerTypePk) {
        List<Customer> customers = customerService.findCustomerByAge(age, LocalDate.parse(date), customerTypePk);
        List<CustomerResponseDto.Response> responses = customerMapper.customersToCustomersResponseDto(customers);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 지역이름으로 정렬
    @GetMapping("/customers/dongName/{dongName}")
    public ResponseEntity findCustomersByDongName(@PathVariable("dongName") String dongName,
                                                  @RequestParam("customerTypePk") long customerTypePk) {
        List<Customer> customers = customerService.findCustomerByDongName(dongName, customerTypePk);
        List<CustomerResponseDto.Response> responses = customerMapper.customersToCustomersResponseDto(customers);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 지역별 정렬
    @GetMapping("/customers")
    public ResponseEntity findCustomersByDong(@RequestParam("dongPk") long dongPk,
                                              @RequestParam("customerTypePk") long customerTypePk) {
        List<Customer> customers = customerService.findCustomerByLi(dongPk, customerTypePk);
        List<CustomerResponseDto.Response> responses = customerMapper.customersToCustomersResponseDto(customers);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 월별 지역별 정렬
    @GetMapping("/customers/{date}")
    public ResponseEntity findCustomersByDongAndMonth(@RequestParam("dongPk") long dongPk,
                                                      @PathVariable("date") String date,
                                                      @RequestParam("customerTypePk") long customerTypePk) {
        List<Customer> customers = customerService.findCustomerByLi(dongPk, LocalDate.parse(date), customerTypePk);
        List<CustomerResponseDto.Response> responses = customerMapper.customersToCustomersResponseDto(customers);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 계약여부 정렬(최신순)
    @GetMapping("/customers/contractYn/{contractYn}/latest")
    public ResponseEntity findCustomersByContractYnByLatest(@PathVariable("contractYn") boolean contractYn,
                                                            @RequestParam("customerTypePk") long customerTypePk) {
        List<Customer> customers = customerService.findCustomerByContractYnByLatest(contractYn, customerTypePk);
        List<CustomerResponseDto.Response> responses = customerMapper.customersToCustomersResponseDto(customers);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 계약여부 정렬(나이대별)
    @GetMapping("/customers/contractYn/{contractYn}/age/{age}")
    public ResponseEntity findCustomersByContractYnByAge(@PathVariable("contractYn") boolean contractYn,
                                                         @PathVariable("age") String age,
                                                         @RequestParam("customerTypePk") long customerTypePk) {
        List<Customer> customers = customerService.findCustomerByContractYnByLatest(contractYn, age, customerTypePk);
        List<CustomerResponseDto.Response> responses = customerMapper.customersToCustomersResponseDto(customers);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 월별 계약여부 정렬
    @GetMapping("/customers/contractYn/{contractYn}/{date}")
    public ResponseEntity findCustomersByContractYnAndMonth(@PathVariable("contractYn") boolean contractYn,
                                                            @PathVariable("date") String date,
                                                            @RequestParam("customerTypePk") long customerTypePk) {
        List<Customer> customers = customerService.findCustomerByContractYn(contractYn, LocalDate.parse(date), customerTypePk);
        List<CustomerResponseDto.Response> responses = customerMapper.customersToCustomersResponseDto(customers);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 상담현황별 정렬
    @GetMapping("/customers/consultationStatus/{consultationStatus}")
    public ResponseEntity findCustomersByConsultationStatusByLatest(@PathVariable("consultationStatus") ConsultationStatus consultationStatus,
                                                                    @RequestParam("customerTypePk") long customerTypePk) {
        List<Customer> customers = customerService.findCustomerByConsultationStatusByLatest(consultationStatus, customerTypePk);
        List<CustomerResponseDto.Response> responses = customerMapper.customersToCustomersResponseDto(customers);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 월별 상담현황별 정렬
    @GetMapping("/customers/consultationStatus/{consultationStatus}/{date}")
    public ResponseEntity findCustomersByConsultationStatusByLatestAndMonth(@PathVariable("consultationStatus") ConsultationStatus consultationStatus,
                                                                            @PathVariable("date") String date,
                                                                            @RequestParam("customerTypePk") long customerTypePk) {
        List<Customer> customers = customerService.findCustomerByConsultationStatusByLatestAndMonth(consultationStatus, LocalDate.parse(date), customerTypePk);
        List<CustomerResponseDto.Response> responses = customerMapper.customersToCustomersResponseDto(customers);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 고객 이름 검색
    @GetMapping("/customers/name/{name}")
    public ResponseEntity findCustomerByname(@PathVariable("name") String name) {
        List<Customer> customers = customerService.findCustomerByName(name);
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
                patch.getMetroGuDong(),
                patch.getCustomerTypePk()
        );
        System.out.println("!! patch.getCustomerTypePk() = " + patch.getCustomerTypePk());
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
                patch.getMetroGuDong(),
                patch.getCustomerTypePk()
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
