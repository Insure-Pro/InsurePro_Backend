package ga.backend.scheduleprogress.mapper;

import ga.backend.scheduleprogress.dto.ScheduleProgressRequestDto;
import ga.backend.scheduleprogress.dto.ScheduleProgressResponseDto;
import ga.backend.scheduleprogress.entity.ScheduleProgress;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScheduleProgressMapper {
    ScheduleProgress scheduleProgressPostDtoToScheduleProgress(ScheduleProgressRequestDto.Post post);
    ScheduleProgress scheduleProgressPatchDtoToScheduleProgress(ScheduleProgressRequestDto.Patch patch);
    ScheduleProgressResponseDto.Response scheduleProgressToScheduleProgressResponseDto(ScheduleProgress scheduleProgress);
}