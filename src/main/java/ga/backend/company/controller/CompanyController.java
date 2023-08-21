package ga.backend.company.controller;

import ga.backend.company.dto.CompanyRequestDto;
import ga.backend.company.dto.CompanyResponseDto;
import ga.backend.company.entity.Company;
import ga.backend.company.mapper.CompanyMapper;
import ga.backend.company.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/company")
@Validated
@AllArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;

    // CREATE
    @PostMapping
    public ResponseEntity postCompany(@Valid @RequestBody CompanyRequestDto.Post post) {
        Company company = companyService.createCompany(companyMapper.companyPostDtoToCompany(post));
        CompanyResponseDto.Response response = companyMapper.companyToCompanyResponseDto(company);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/{company-pk}")
    public ResponseEntity getCompany(@Positive @PathVariable("company-pk") long companyPk) {
        Company company = companyService.findCompany(companyPk);
        CompanyResponseDto.Response response = companyMapper.companyToCompanyResponseDto(company);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // UPDATE
    @PatchMapping
    public ResponseEntity patchCompany(@Valid @RequestBody CompanyRequestDto.Patch patch) {
        Company company = companyService.patchCompany(companyMapper.companyPatchDtoToCompany(patch));
        CompanyResponseDto.Response response = companyMapper.companyToCompanyResponseDto(company);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // DELETE
    @DeleteMapping("/{company-pk}")
    public ResponseEntity deleteCompany(@Positive @PathVariable("company-pk") long companyPk) {
        companyService.deleteCompany(companyPk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
