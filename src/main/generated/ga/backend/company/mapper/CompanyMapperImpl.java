package ga.backend.company.mapper;

import ga.backend.company.dto.CompanyRequestDto;
import ga.backend.company.dto.CompanyResponseDto;
import ga.backend.company.entity.Company;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-28T15:44:10+0900",
    comments = "version: 1.5.1.Final, compiler: javac, environment: Java 11.0.16.1 (Oracle Corporation)"
)
@Component
public class CompanyMapperImpl implements CompanyMapper {

    @Override
    public Company companyPostDtoToCompany(CompanyRequestDto.Post post) {
        if ( post == null ) {
            return null;
        }

        Company company = new Company();

        company.setPk( post.getPk() );
        company.setName( post.getName() );
        company.setDelYn( post.isDelYn() );

        return company;
    }

    @Override
    public Company companyPatchDtoToCompany(CompanyRequestDto.Patch patch) {
        if ( patch == null ) {
            return null;
        }

        Company company = new Company();

        company.setPk( patch.getPk() );
        company.setName( patch.getName() );
        company.setDelYn( patch.isDelYn() );

        return company;
    }

    @Override
    public CompanyResponseDto.Response companyToCompanyResponseDto(Company company) {
        if ( company == null ) {
            return null;
        }

        Long pk = null;
        String name = null;
        boolean delYn = false;
        LocalDateTime createdAt = null;
        LocalDateTime modifiedAt = null;

        pk = company.getPk();
        name = company.getName();
        delYn = company.isDelYn();
        createdAt = company.getCreatedAt();
        modifiedAt = company.getModifiedAt();

        CompanyResponseDto.Response response = new CompanyResponseDto.Response( pk, name, delYn, createdAt, modifiedAt );

        return response;
    }
}
