package ga.backend.company.mapper;

import ga.backend.company.dto.CompanyRequestDto;
import ga.backend.company.dto.CompanyResponseDto;
import ga.backend.company.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    @Mapping(source = "post.delYn", target="delYn", ignore=true)
    Company companyPostDtoToCompany(CompanyRequestDto.Post post);

    @Mapping(source = "patch.delYn", target="delYn", ignore=true)
    Company companyPatchDtoToCompany(CompanyRequestDto.Patch patch);
    CompanyResponseDto.Response companyToCompanyResponseDto(Company company);
    List<CompanyResponseDto.Response> companyToCompanyListResponseDto(List<Company> companys);

}