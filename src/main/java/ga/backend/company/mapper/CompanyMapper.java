package ga.backend.company.mapper;

import ga.backend.company.dto.CompanyRequestDto;
import ga.backend.company.dto.CompanyResponseDto;
import ga.backend.company.entity.Company;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    Company companyPostDtoToCompany(CompanyRequestDto.Post post);
    Company companyPatchDtoToCompany(CompanyRequestDto.Patch patch);
    CompanyResponseDto.Response companyToCompanyResponseDto(Company company);
}