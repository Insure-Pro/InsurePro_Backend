package ga.backend.dong.mapper;

import ga.backend.dong.dto.DongRequestDto;
import ga.backend.dong.dto.DongResponseDto;
import ga.backend.dong.entity.Dong;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-04T21:03:21+0900",
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
        dong.setDong( post.getDong() );
        dong.setDelYn( post.getDelYn() );

        return dong;
    }

    @Override
    public Dong dongPatchDtoToDong(DongRequestDto.Patch patch) {
        if ( patch == null ) {
            return null;
        }

        Dong dong = new Dong();

        dong.setPk( patch.getPk() );
        dong.setDong( patch.getDong() );
        dong.setDelYn( patch.getDelYn() );

        return dong;
    }

    @Override
    public DongResponseDto.Response dongToDongResponseDto(Dong dong) {
        if ( dong == null ) {
            return null;
        }

        Long pk = null;
        String dong1 = null;
        Boolean delYn = null;
        LocalDateTime createdAt = null;
        LocalDateTime modifiedAt = null;

        pk = dong.getPk();
        dong1 = dong.getDong();
        delYn = dong.getDelYn();
        createdAt = dong.getCreatedAt();
        modifiedAt = dong.getModifiedAt();

        DongResponseDto.Response response = new DongResponseDto.Response( pk, dong1, delYn, createdAt, modifiedAt );

        return response;
    }

    @Override
    public List<DongResponseDto.Response> dongToListDongResponseDto(List<Dong> dong) {
        if ( dong == null ) {
            return null;
        }

        List<DongResponseDto.Response> list = new ArrayList<DongResponseDto.Response>( dong.size() );
        for ( Dong dong1 : dong ) {
            list.add( dongToDongResponseDto( dong1 ) );
        }

        return list;
    }
}
