package ga.backend.dayschedule.mapper;

import ga.backend.dayschedule.dto.DayScheduleRequestDto;
import ga.backend.dayschedule.dto.DayScheduleResponseDto;
import ga.backend.dayschedule.entity.DaySchedule;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-04T21:03:21+0900",
    comments = "version: 1.5.1.Final, compiler: javac, environment: Java 11.0.16.1 (Oracle Corporation)"
)
@Component
public class DayScheduleMapperImpl implements DayScheduleMapper {

    @Override
    public DaySchedule daySchedulePostDtoToDaySchedule(DayScheduleRequestDto.Post post) {
        if ( post == null ) {
            return null;
        }

        DaySchedule daySchedule = new DaySchedule();

        daySchedule.setPk( post.getPk() );
        daySchedule.setEmployee( post.getEmployee() );
        daySchedule.setContent( post.getContent() );
        daySchedule.setDoYn( post.isDoYn() );

        return daySchedule;
    }

    @Override
    public DaySchedule daySchedulePatchDtoToDaySchedule(DayScheduleRequestDto.Patch patch) {
        if ( patch == null ) {
            return null;
        }

        DaySchedule daySchedule = new DaySchedule();

        daySchedule.setPk( patch.getPk() );
        daySchedule.setEmployee( patch.getEmployee() );
        daySchedule.setContent( patch.getContent() );
        daySchedule.setDoYn( patch.isDoYn() );

        return daySchedule;
    }

    @Override
    public DayScheduleResponseDto.Response dayScheduleToDayScheduleResponseDto(DaySchedule daySchedule) {
        if ( daySchedule == null ) {
            return null;
        }

        Long pk = null;
        LocalDateTime createdAt = null;
        LocalDateTime modifiedAt = null;

        pk = daySchedule.getPk();
        createdAt = daySchedule.getCreatedAt();
        modifiedAt = daySchedule.getModifiedAt();

        String name = null;
        boolean delYn = false;

        DayScheduleResponseDto.Response response = new DayScheduleResponseDto.Response( pk, name, delYn, createdAt, modifiedAt );

        return response;
    }
}
