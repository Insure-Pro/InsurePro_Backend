package ga.backend.customerType.controller;

import ga.backend.customerType.dto.CustomerTypeRequestDto;
import ga.backend.customerType.dto.CustomerTypeResponseDto;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.customerType.mapper.CustomerTypeMapper;
import ga.backend.customerType.service.CustomerTypeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/customerType")
@Validated
@AllArgsConstructor
public class CustomerTypeController {
    private final CustomerTypeService customerTypeService;
    private final CustomerTypeMapper customerTypeMapper;

    // CREATE
    @PostMapping
    public ResponseEntity postCustomerType(@Valid @RequestBody CustomerTypeRequestDto.Post post) {
        CustomerType customerType = customerTypeService.createCustomerType(customerTypeMapper.customerTypePostDtoToCustomerType(post));
        CustomerTypeResponseDto.Response response = customerTypeMapper.customerTypeToCustomerTypeResponseDto(customerType);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/{customerType-pk}")
    public ResponseEntity getCustomerType(@Positive @PathVariable("customerType-pk") long customerTypePk) {
        CustomerType customerType = customerTypeService.findCustomerType(customerTypePk);
        CustomerTypeResponseDto.Response response = customerTypeMapper.customerTypeToCustomerTypeResponseDto(customerType);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // UPDATE
    @PatchMapping
    public ResponseEntity patchCustomerType(@Valid @RequestBody CustomerTypeRequestDto.Patch patch) {
        CustomerType customerType = customerTypeService.patchCustomerType(customerTypeMapper.customerTypePatchDtoToCustomerType(patch));
        CustomerTypeResponseDto.Response response = customerTypeMapper.customerTypeToCustomerTypeResponseDto(customerType);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // DELETE
    @DeleteMapping("/{customerType-pk}")
    public ResponseEntity deleteCustomerType(@Positive @PathVariable("customerType-pk") long customerTypePk) {
        customerTypeService.deleteCustomerType(customerTypePk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}