package ga.backend.progress.mapper;

import ga.backend.progress.dto.ProgressRequestDto;
import ga.backend.progress.dto.ProgressResponseDto;
import ga.backend.progress.entity.Progress;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-28T15:44:09+0900",
    comments = "version: 1.5.1.Final, compiler: javac, environment: Java 11.0.16.1 (Oracle Corporation)"
)
@Component
public class ProgressMapperImpl implements ProgressMapper {

    @Override
    public Progress progressPostDtoToProgress(ProgressRequestDto.Post post) {
        if ( post == null ) {
            return null;
        }

        Progress progress = new Progress();

        progress.setPk( post.getPk() );
        progress.setOptionName( post.getOptionName() );
        progress.setDelYn( post.isDelYn() );

        return progress;
    }

    @Override
    public Progress progressPatchDtoToProgress(ProgressRequestDto.Patch patch) {
        if ( patch == null ) {
            return null;
        }

        Progress progress = new Progress();

        progress.setPk( patch.getPk() );
        progress.setOptionName( patch.getOptionName() );
        progress.setDelYn( patch.isDelYn() );

        return progress;
    }

    @Override
    public ProgressResponseDto.Response progressToProgressResponseDto(Progress progress) {
        if ( progress == null ) {
            return null;
        }

        Long pk = null;
        String optionName = null;
        boolean delYn = false;
        LocalDateTime createdAt = null;
        LocalDateTime modifiedAt = null;

        pk = progress.getPk();
        optionName = progress.getOptionName();
        delYn = progress.isDelYn();
        createdAt = progress.getCreatedAt();
        modifiedAt = progress.getModifiedAt();

        ProgressResponseDto.Response response = new ProgressResponseDto.Response( pk, optionName, delYn, createdAt, modifiedAt );

        return response;
    }
}
