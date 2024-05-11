package ga.backend.schedule.mapper;

import ga.backend.contract.entity.Contract;
import ga.backend.schedule.dto.ScheduleRequestDto;
import ga.backend.schedule.dto.ScheduleResponseDto;
import ga.backend.schedule.entity.Schedule;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    Schedule schedulePostDtoToSchedule(ScheduleRequestDto.Post post);
    Schedule schedulePatchDtoToSchedule(ScheduleRequestDto.Patch patch);
    ScheduleResponseDto.Response scheduleToScheduleResponseDto(Schedule schedule);
    List<ScheduleResponseDto.Response> schedulesToSchedulesResponseDto(List<Schedule> schedule);
}