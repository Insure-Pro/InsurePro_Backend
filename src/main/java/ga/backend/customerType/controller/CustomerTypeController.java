package ga.backend.customerType.controller;

import ga.backend.customerType.dto.CustomerTypeRequestDto;
import ga.backend.customerType.dto.CustomerTypeResponseDto;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.customerType.mapper.CustomerTypeMapper;
import ga.backend.customerType.service.CustomerTypeService;
import ga.backend.util.Version;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(Version.currentUrl)
@Validated
@AllArgsConstructor
public class CustomerTypeController {
    private final CustomerTypeService customerTypeService;
    private final CustomerTypeMapper customerTypeMapper;

    // CREATE
    @PostMapping("/customertype")
    public ResponseEntity postCustomerType(@Valid @RequestBody CustomerTypeRequestDto.Post post) {
        CustomerType customerType = customerTypeService.createCustomerType(customerTypeMapper.customerTypePostDtoToCustomerType(post));
        CustomerTypeResponseDto.Response response = customerTypeMapper.customerTypeToCustomerTypeResponseDto(customerType);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/customertypes/{customerType-pk}")
    public ResponseEntity getCustomerType(@Positive @PathVariable("customerType-pk") long customerTypePk) {
        CustomerType customerType = customerTypeService.findCustomerType(customerTypePk);
        CustomerTypeResponseDto.Response response = customerTypeMapper.customerTypeToCustomerTypeResponseDto(customerType);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // UPDATE
    @PatchMapping("/customertype/{customerType-pk}")
    public ResponseEntity patchCustomerType(@Positive @PathVariable("customerType-pk") long customerTypePk,
                                            @Valid @RequestBody CustomerTypeRequestDto.Patch patch) {
        patch.setPk(customerTypePk);
        System.out.println("!! delYn : " + patch.getDelYn());
        CustomerType customerType = customerTypeService.patchCustomerType(customerTypeMapper.customerTypePatchDtoToCustomerType(patch));
        CustomerTypeResponseDto.Response response = customerTypeMapper.customerTypeToCustomerTypeResponseDto(customerType);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/customertype/{customerType-pk}")
    public ResponseEntity deleteCustomerType(@Positive @PathVariable("customerType-pk") long customerTypePk) {
        customerTypeService.deleteCustomerType(customerTypePk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
