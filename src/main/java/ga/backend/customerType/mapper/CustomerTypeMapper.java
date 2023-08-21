package ga.backend.customerType.mapper;

import ga.backend.customerType.dto.CustomerTypeRequestDto;
import ga.backend.customerType.dto.CustomerTypeResponseDto;
import ga.backend.customerType.entity.CustomerType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerTypeMapper {
    CustomerType customerTypePostDtoToCustomerType(CustomerTypeRequestDto.Post post);
    CustomerType customerTypePatchDtoToCustomerType(CustomerTypeRequestDto.Patch patch);
    CustomerTypeResponseDto.Response customerTypeToCustomerTypeResponseDto(CustomerType customerType);
}