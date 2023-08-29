package ga.backend.schedule.mapper;

import ga.backend.schedule.dto.ScheduleRequestDto;
import ga.backend.schedule.dto.ScheduleResponseDto;
import ga.backend.schedule.entity.Schedule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-29T11:53:32+0900",
    comments = "version: 1.5.1.Final, compiler: javac, environment: Java 11.0.16.1 (Oracle Corporation)"
)
@Component
public class ScheduleMapperImpl implements ScheduleMapper {

    @Override
    public Schedule schedulePostDtoToSchedule(ScheduleRequestDto.Post post) {
        if ( post == null ) {
            return null;
        }

        Schedule schedule = new Schedule();

        schedule.setPk( post.getPk() );
        schedule.setMemo( post.getMemo() );
        schedule.setDate( post.getDate() );
        schedule.setStartTm( post.getStartTm() );
        schedule.setFinishTm( post.getFinishTm() );
        schedule.setTime( post.getTime() );
        schedule.setAddress( post.getAddress() );
        schedule.setColor( post.getColor() );
        schedule.setMeetYn( post.isMeetYn() );
        schedule.setDelYn( post.isDelYn() );

        return schedule;
    }

    @Override
    public Schedule schedulePatchDtoToSchedule(ScheduleRequestDto.Patch patch) {
        if ( patch == null ) {
            return null;
        }

        Schedule schedule = new Schedule();

        schedule.setPk( patch.getPk() );
        schedule.setMemo( patch.getMemo() );
        schedule.setDate( patch.getDate() );
        schedule.setStartTm( patch.getStartTm() );
        schedule.setFinishTm( patch.getFinishTm() );
        schedule.setTime( patch.getTime() );
        schedule.setAddress( patch.getAddress() );
        schedule.setColor( patch.getColor() );
        schedule.setMeetYn( patch.isMeetYn() );
        schedule.setDelYn( patch.isDelYn() );

        return schedule;
    }

    @Override
    public ScheduleResponseDto.Response scheduleToScheduleResponseDto(Schedule schedule) {
        if ( schedule == null ) {
            return null;
        }

        Long pk = null;
        String memo = null;
        LocalDate date = null;
        LocalTime startTm = null;
        LocalTime finishTm = null;
        LocalTime time = null;
        String address = null;
        boolean meetYn = false;
        boolean delYn = false;
        String color = null;
        LocalDateTime createdAt = null;
        LocalDateTime modifiedAt = null;

        pk = schedule.getPk();
        memo = schedule.getMemo();
        date = schedule.getDate();
        startTm = schedule.getStartTm();
        finishTm = schedule.getFinishTm();
        time = schedule.getTime();
        address = schedule.getAddress();
        meetYn = schedule.isMeetYn();
        delYn = schedule.isDelYn();
        color = schedule.getColor();
        createdAt = schedule.getCreatedAt();
        modifiedAt = schedule.getModifiedAt();

        ScheduleResponseDto.Response response = new ScheduleResponseDto.Response( pk, memo, date, startTm, finishTm, time, address, meetYn, delYn, color, createdAt, modifiedAt );

        return response;
    }
}
