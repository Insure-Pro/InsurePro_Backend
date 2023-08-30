package ga.backend.customerType.mapper;

import ga.backend.customerType.dto.CustomerTypeRequestDto;
import ga.backend.customerType.dto.CustomerTypeResponseDto;
import ga.backend.customerType.entity.CustomerType;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-29T11:53:32+0900",
    comments = "version: 1.5.1.Final, compiler: javac, environment: Java 11.0.16.1 (Oracle Corporation)"
)
@Component
public class CustomerTypeMapperImpl implements CustomerTypeMapper {

    @Override
    public CustomerType customerTypePostDtoToCustomerType(CustomerTypeRequestDto.Post post) {
        if ( post == null ) {
            return null;
        }

        CustomerType customerType = new CustomerType();

        customerType.setPk( post.getPk() );
        customerType.setType( post.getType() );
        customerType.setDetail( post.getDetail() );
        customerType.setDelYn( post.isDelYn() );

        return customerType;
    }

    @Override
    public CustomerType customerTypePatchDtoToCustomerType(CustomerTypeRequestDto.Patch patch) {
        if ( patch == null ) {
            return null;
        }

        CustomerType customerType = new CustomerType();

        customerType.setPk( patch.getPk() );
        customerType.setType( patch.getType() );
        customerType.setDetail( patch.getDetail() );
        customerType.setDelYn( patch.isDelYn() );

        return customerType;
    }

    @Override
    public CustomerTypeResponseDto.Response customerTypeToCustomerTypeResponseDto(CustomerType customerType) {
        if ( customerType == null ) {
            return null;
        }

        Long pk = null;
        String type = null;
        String detail = null;
        boolean delYn = false;
        LocalDateTime createdAt = null;
        LocalDateTime modifiedAt = null;

        pk = customerType.getPk();
        type = customerType.getType();
        detail = customerType.getDetail();
        delYn = customerType.isDelYn();
        createdAt = customerType.getCreatedAt();
        modifiedAt = customerType.getModifiedAt();

        CustomerTypeResponseDto.Response response = new CustomerTypeResponseDto.Response( pk, type, detail, delYn, createdAt, modifiedAt );

        return response;
    }
}
