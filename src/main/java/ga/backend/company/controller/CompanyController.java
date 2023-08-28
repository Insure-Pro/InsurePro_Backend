package ga.backend.company.controller;

import ga.backend.company.dto.CompanyRequestDto;
import ga.backend.company.dto.CompanyResponseDto;
import ga.backend.company.entity.Company;
import ga.backend.company.mapper.CompanyMapper;
import ga.backend.company.service.CompanyService;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
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
@RequestMapping(Version.currentUrl + "/companys")
@Validated
@AllArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;

    /**
     *
     * @param post
     * @return {
     * 	"company_pk" : 1,
     * 	"name" : "패스파인더"
     * }
     */
    @PostMapping
    public ResponseEntity postCompany(@Valid @RequestBody CompanyRequestDto.Post post) {

        // 생성
        Company company = companyService.createCompany(companyMapper.companyPostDtoToCompany(post));

        // 응답
        CompanyResponseDto.Response response = companyMapper.companyToCompanyResponseDto(company);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     *
     * @param pk
     * @param name
     * @return {
     *     "companys" : [
     *         {
     *             "company_pk" : "1",
     *             "name" : "ASCompany"
     *         },
     *         {
     *             "company_pk" : "2",
     *             "name" : "패스파인더"
     *         },
     *     ]
     * }
     */
    @GetMapping
    public ResponseEntity getCompanyList(
            @Positive @RequestParam(value = "pk", required = false) Long pk,
            @RequestParam(value = "name", required = false) String name) {

        // 조회
        List<Company> findCompanys = companyService.findCompanys(pk, name);

        // 응답
        List<CompanyResponseDto.Response> companys = companyMapper.companyToCompanyListResponseDto(findCompanys);
        JSONObject response = new JSONObject();
        response.put("companys", companys);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     *
     * @param companyPk
     * @param patch
     * @return {
     * 	"company_pk" : 1,
     * 	"name" : "SAMSUNG"
     * }
     */
    @PatchMapping("/{company_pk}")
    public ResponseEntity patchCompany(@Positive @PathVariable("company_pk") long companyPk,
                                       @Valid @RequestBody CompanyRequestDto.Patch patch) {
        // 수정
        patch.setPk(companyPk);
        Company company = companyService.patchCompany(companyMapper.companyPatchDtoToCompany(patch));

        // 응답
        CompanyResponseDto.Response response = companyMapper.companyToCompanyResponseDto(company);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     *
     * @param companyPk
     * @return {
     *     "company": {
     *         "pk": 4,
     *         "name": "company2",
     *         "delYn": true,
     *         "createdAt": "2023-08-28T16:45:47.017454",
     *         "modifiedAt": "2023-08-29T12:26:59.471178"
     *     },
     *     "message": "success delete"
     * }
     */
    @DeleteMapping("/{company_pk}")
    public ResponseEntity deleteCompany(@Positive @PathVariable("company_pk") long companyPk) {
        // 삭제
        Company company = companyService.deleteCompany(companyPk);

        // 응답
        CompanyResponseDto.Response companyResponse = companyMapper.companyToCompanyResponseDto(company);
        JSONObject response = new JSONObject();
        response.put("company", companyResponse);
        response.put("message", "delete successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
