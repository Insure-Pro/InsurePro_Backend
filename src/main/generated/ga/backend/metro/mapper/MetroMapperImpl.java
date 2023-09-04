package ga.backend.metro.mapper;

import ga.backend.metro.dto.MetroRequestDto;
import ga.backend.metro.dto.MetroResponseDto;
import ga.backend.metro.entity.Metro;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-04T21:03:20+0900",
    comments = "version: 1.5.1.Final, compiler: javac, environment: Java 11.0.16.1 (Oracle Corporation)"
)
@Component
public class MetroMapperImpl implements MetroMapper {

    @Override
    public Metro metroPostDtoToMetro(MetroRequestDto.Post post) {
        if ( post == null ) {
            return null;
        }

        Metro metro = new Metro();

        metro.setPk( post.getPk() );
        metro.setMetro( post.getMetro() );
        metro.setDelYn( post.getDelYn() );

        return metro;
    }

    @Override
    public Metro metroPatchDtoToMetro(MetroRequestDto.Patch patch) {
        if ( patch == null ) {
            return null;
        }

        Metro metro = new Metro();

        metro.setPk( patch.getPk() );
        metro.setMetro( patch.getMetro() );
        metro.setDelYn( patch.getDelYn() );

        return metro;
    }

    @Override
    public MetroResponseDto.Response metroToMetroResponseDto(Metro metro) {
        if ( metro == null ) {
            return null;
        }

        Long pk = null;
        String metro1 = null;
        Boolean delYn = null;
        LocalDateTime createdAt = null;
        LocalDateTime modifiedAt = null;

        pk = metro.getPk();
        metro1 = metro.getMetro();
        delYn = metro.getDelYn();
        createdAt = metro.getCreatedAt();
        modifiedAt = metro.getModifiedAt();

        MetroResponseDto.Response response = new MetroResponseDto.Response( pk, metro1, delYn, createdAt, modifiedAt );

        return response;
    }

    @Override
    public List<MetroResponseDto.Response> metroToListMetroResponseDto(List<Metro> metros) {
        if ( metros == null ) {
            return null;
        }

        List<MetroResponseDto.Response> list = new ArrayList<MetroResponseDto.Response>( metros.size() );
        for ( Metro metro : metros ) {
            list.add( metroToMetroResponseDto( metro ) );
        }

        return list;
    }
}
