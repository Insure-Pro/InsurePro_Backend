package ga.backend.authorizationNumber.mapper;

import ga.backend.authorizationNumber.dto.AuthorizationNumberRequestDto;
import ga.backend.authorizationNumber.dto.AuthorizationNumberResponseDto;
import ga.backend.authorizationNumber.entity.AuthorizationNumber;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-04T21:03:21+0900",
    comments = "version: 1.5.1.Final, compiler: javac, environment: Java 11.0.16.1 (Oracle Corporation)"
)
@Component
public class AuthorizationNumberMapperImpl implements AuthorizationNumberMapper {

    @Override
    public AuthorizationNumber authorizationNumberPostDtoToAuthorizationNumber(AuthorizationNumberRequestDto.Post post) {
        if ( post == null ) {
            return null;
        }

        AuthorizationNumber authorizationNumber = new AuthorizationNumber();

        authorizationNumber.setEmail( post.getEmail() );

        return authorizationNumber;
    }

    @Override
    public AuthorizationNumber authorizationNumberCheckDtoToAuthorizationNumber(AuthorizationNumberRequestDto.Check check) {
        if ( check == null ) {
            return null;
        }

        AuthorizationNumber authorizationNumber = new AuthorizationNumber();

        authorizationNumber.setEmail( check.getEmail() );
        authorizationNumber.setAuthNum( check.getAuthNum() );

        return authorizationNumber;
    }

    @Override
    public AuthorizationNumberResponseDto.Response authorizationNumberToAuthorizationNumberResponseDto(AuthorizationNumber authorizationNumber) {
        if ( authorizationNumber == null ) {
            return null;
        }

        String email = null;
        int authNum = 0;

        email = authorizationNumber.getEmail();
        authNum = authorizationNumber.getAuthNum();

        AuthorizationNumberResponseDto.Response response = new AuthorizationNumberResponseDto.Response( email, authNum );

        return response;
    }
}
