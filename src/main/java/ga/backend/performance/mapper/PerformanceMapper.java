package ga.backend.performance.mapper;

import ga.backend.performance.dto.PerformanceRequestDto;
import ga.backend.performance.dto.PerformanceResponseDto;
import ga.backend.performance.entity.Performance;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PerformanceMapper {
    Performance performancePostDtoToPerformance(PerformanceRequestDto.Post post);
    Performance performancePatchDtoToPerformance(PerformanceRequestDto.Patch patch);
    PerformanceResponseDto.Response performanceToPerformanceResponseDto(Performance performance);
}