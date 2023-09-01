package ga.backend.customerType.controller;

import com.nimbusds.jose.shaded.json.JSONObject;
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
@RequestMapping(Version.currentUrl + "/customertypes")
@Validated
@AllArgsConstructor
public class CustomerTypeController {
    private final CustomerTypeService customerTypeService;
    private final CustomerTypeMapper customerTypeMapper;

    // CREATE
    @PostMapping
    public ResponseEntity postCustomerType(@Valid @RequestBody CustomerTypeRequestDto.Post post) {

        CustomerType customerType = customerTypeService.createCustomerType(customerTypeMapper.customerTypePostDtoToCustomerType(post), post.getCompany_pk());
        CustomerTypeResponseDto.Response response = customerTypeMapper.customerTypeToCustomerTypeResponseDto(customerType);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/{customer_type_pk}")
    public ResponseEntity getCustomerType(@Positive @PathVariable("customer_type_pk") long customerTypePk) {
        CustomerType customerType = customerTypeService.findCustomerType(customerTypePk);
        CustomerTypeResponseDto.Response response = customerTypeMapper.customerTypeToCustomerTypeResponseDto(customerType);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // UPDATE
    @PatchMapping
    public ResponseEntity patchCustomerType(@Valid @RequestBody CustomerTypeRequestDto.Patch patch) {
        CustomerType customerType = customerTypeService.patchCustomerType(customerTypeMapper.customerTypePatchDtoToCustomerType(patch));
        CustomerTypeResponseDto.Response customerTypeResponse = customerTypeMapper.customerTypeToCustomerTypeResponseDto(customerType);
        JSONObject response = new JSONObject();
        response.put("companyTypes", customerTypeResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/{customerType-pk}")
    public ResponseEntity deleteCustomerType(@Positive @PathVariable("customerType-pk") long customerTypePk) {
        customerTypeService.deleteCustomerType(customerTypePk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
