package ga.backend.company.controller;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import ga.backend.company.dto.CompanyRequestDto;
import ga.backend.company.dto.CompanyResponseDto;
import ga.backend.company.entity.Company;
import ga.backend.company.mapper.CompanyMapper;
import ga.backend.company.service.CompanyService;
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
@RequestMapping(Version.currentUrl + "/company")
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

    @GetMapping
    public ResponseEntity getCompanyList(
            @RequestParam(value = "pk", required = false) Long pk,
            @RequestParam(value = "name", required = false) String name) {
        // pk, name Repuest Param에 따른 Company 리스트
        List<Company> findCompanys = companyService.findCompanys(pk, name);
        List<CompanyResponseDto.Response> companys = companyMapper.companyToCompanyListResponseDto(findCompanys);

        // {"companys" : []} 형식으로 변환
        JSONObject response = new JSONObject();
        response.put("companys", companys);
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
