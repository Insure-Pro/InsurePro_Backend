package ga.backend.gu.mapper;

import ga.backend.gu.dto.GuRequestDto;
import ga.backend.gu.dto.GuResponseDto;
import ga.backend.gu.entity.Gu;
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
public class GuMapperImpl implements GuMapper {

    @Override
    public Gu guPostDtoToGu(GuRequestDto.Post post) {
        if ( post == null ) {
            return null;
        }

        Gu gu = new Gu();

        gu.setPk( post.getPk() );
        gu.setGu( post.getGu() );
        gu.setDelYn( post.getDelYn() );

        return gu;
    }

    @Override
    public Gu guPatchDtoToGu(GuRequestDto.Patch patch) {
        if ( patch == null ) {
            return null;
        }

        Gu gu = new Gu();

        gu.setPk( patch.getPk() );
        gu.setGu( patch.getGu() );
        gu.setDelYn( patch.getDelYn() );

        return gu;
    }

    @Override
    public GuResponseDto.Response guToGuResponseDto(Gu gu) {
        if ( gu == null ) {
            return null;
        }

        Long pk = null;
        String gu1 = null;
        Boolean delYn = null;
        LocalDateTime createdAt = null;
        LocalDateTime modifiedAt = null;

        pk = gu.getPk();
        gu1 = gu.getGu();
        delYn = gu.getDelYn();
        createdAt = gu.getCreatedAt();
        modifiedAt = gu.getModifiedAt();

        GuResponseDto.Response response = new GuResponseDto.Response( pk, gu1, delYn, createdAt, modifiedAt );

        return response;
    }

    @Override
    public List<GuResponseDto.Response> guToListGuResponseDto(List<Gu> gu) {
        if ( gu == null ) {
            return null;
        }

        List<GuResponseDto.Response> list = new ArrayList<GuResponseDto.Response>( gu.size() );
        for ( Gu gu1 : gu ) {
            list.add( guToGuResponseDto( gu1 ) );
        }

        return list;
    }
}
