package ga.backend.company.mapper;

import ga.backend.company.dto.CompanyRequestDto;
import ga.backend.company.dto.CompanyResponseDto;
import ga.backend.company.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    @Mapping(source = "post.delYn", target="delYn", ignore=true)
    Company companyPostDtoToCompany(CompanyRequestDto.Post post);
    Company companyPatchDtoToCompany(CompanyRequestDto.Patch patch);
    CompanyResponseDto.Response companyToCompanyResponseDto(Company company);
}