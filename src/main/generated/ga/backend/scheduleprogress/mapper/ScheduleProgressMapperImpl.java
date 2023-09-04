package ga.backend.scheduleprogress.mapper;

import ga.backend.scheduleprogress.dto.ScheduleProgressRequestDto;
import ga.backend.scheduleprogress.dto.ScheduleProgressResponseDto;
import ga.backend.scheduleprogress.entity.ScheduleProgress;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-04T21:03:20+0900",
    comments = "version: 1.5.1.Final, compiler: javac, environment: Java 11.0.16.1 (Oracle Corporation)"
)
@Component
public class ScheduleProgressMapperImpl implements ScheduleProgressMapper {

    @Override
    public ScheduleProgress scheduleProgressPostDtoToScheduleProgress(ScheduleProgressRequestDto.Post post) {
        if ( post == null ) {
            return null;
        }

        ScheduleProgress scheduleProgress = new ScheduleProgress();

        scheduleProgress.setPk( post.getPk() );

        return scheduleProgress;
    }

    @Override
    public ScheduleProgress scheduleProgressPatchDtoToScheduleProgress(ScheduleProgressRequestDto.Patch patch) {
        if ( patch == null ) {
            return null;
        }

        ScheduleProgress scheduleProgress = new ScheduleProgress();

        scheduleProgress.setPk( patch.getPk() );

        return scheduleProgress;
    }

    @Override
    public ScheduleProgressResponseDto.Response scheduleProgressToScheduleProgressResponseDto(ScheduleProgress scheduleProgress) {
        if ( scheduleProgress == null ) {
            return null;
        }

        Long pk = null;
        LocalDateTime createdAt = null;
        LocalDateTime modifiedAt = null;

        pk = scheduleProgress.getPk();
        createdAt = scheduleProgress.getCreatedAt();
        modifiedAt = scheduleProgress.getModifiedAt();

        ScheduleProgressResponseDto.Response response = new ScheduleProgressResponseDto.Response( pk, createdAt, modifiedAt );

        return response;
    }
}
