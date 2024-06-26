package ga.backend.ta.mapper;

import ga.backend.ta.dto.TARequestDto;
import ga.backend.ta.dto.TAResponseDto;
import ga.backend.ta.entity.TA;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TAMapper {
    TA taPostDtoToTA(TARequestDto.Post post);
    TA taPatchDtoToTA(TARequestDto.Patch patch);
    TAResponseDto.Response taToTAResponseDto(TA ta);
    List<TAResponseDto.Response> tasToTAResponseDtos(List<TA> tas);
}