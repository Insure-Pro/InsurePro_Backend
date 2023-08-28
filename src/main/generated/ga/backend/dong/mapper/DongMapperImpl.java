package ga.backend.dong.mapper;

import ga.backend.dong.dto.DongRequestDto;
import ga.backend.dong.dto.DongResponseDto;
import ga.backend.dong.entity.Dong;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-28T15:44:09+0900",
    comments = "version: 1.5.1.Final, compiler: javac, environment: Java 11.0.16.1 (Oracle Corporation)"
)
@Component
public class DongMapperImpl implements DongMapper {

    @Override
    public Dong dongPostDtoToDong(DongRequestDto.Post post) {
        if ( post == null ) {
            return null;
        }

        Dong dong = new Dong();

        dong.setPk( post.getPk() );
        dong.setSi( post.getSi() );
        dong.setGu( post.getGu() );
        dong.setDong( post.getDong() );
        dong.setDelYn( post.isDelYn() );

        return dong;
    }

    @Override
    public Dong dongPatchDtoToDong(DongRequestDto.Patch patch) {
        if ( patch == null ) {
            return null;
        }

        Dong dong = new Dong();

        dong.setPk( patch.getPk() );
        dong.setSi( patch.getSi() );
        dong.setGu( patch.getGu() );
        dong.setDong( patch.getDong() );
        dong.setDelYn( patch.isDelYn() );

        return dong;
    }

    @Override
    public DongResponseDto.Response dongToDongResponseDto(Dong dong) {
        if ( dong == null ) {
            return null;
        }

        Long pk = null;
        boolean delYn = false;
        LocalDateTime createdAt = null;
        LocalDateTime modifiedAt = null;

        pk = dong.getPk();
        delYn = dong.isDelYn();
        createdAt = dong.getCreatedAt();
        modifiedAt = dong.getModifiedAt();

        String name = null;

        DongResponseDto.Response response = new DongResponseDto.Response( pk, name, delYn, createdAt, modifiedAt );

        return response;
    }
}
