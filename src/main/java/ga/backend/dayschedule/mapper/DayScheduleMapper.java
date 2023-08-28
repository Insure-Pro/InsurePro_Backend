package ga.backend.dayschedule.mapper;

import ga.backend.dayschedule.dto.DayScheduleRequestDto;
import ga.backend.dayschedule.dto.DayScheduleResponseDto;
import ga.backend.dayschedule.entity.DaySchedule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DayScheduleMapper {
    DaySchedule daySchedulePostDtoToDaySchedule(DayScheduleRequestDto.Post post);
    DaySchedule daySchedulePatchDtoToDaySchedule(DayScheduleRequestDto.Patch patch);
    DayScheduleResponseDto.Response dayScheduleToDayScheduleResponseDto(DaySchedule daySchedule);
}