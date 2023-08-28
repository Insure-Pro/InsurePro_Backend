package ga.backend.authorizationNumber.mapper;

import ga.backend.authorizationNumber.dto.AuthorizationNumberRequestDto;
import ga.backend.authorizationNumber.dto.AuthorizationNumberResponseDto;
import ga.backend.authorizationNumber.entity.AuthorizationNumber;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorizationNumberMapper {
    AuthorizationNumber authorizationNumberPostDtoToAuthorizationNumber(AuthorizationNumberRequestDto.Post post);
    AuthorizationNumber authorizationNumberCheckDtoToAuthorizationNumber(AuthorizationNumberRequestDto.Check check);
    AuthorizationNumberResponseDto.Response authorizationNumberToAuthorizationNumberResponseDto(AuthorizationNumber authorizationNumber);
}