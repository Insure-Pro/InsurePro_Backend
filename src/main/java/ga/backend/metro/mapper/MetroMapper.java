package ga.backend.metro.mapper;

import ga.backend.metro.dto.MetroRequestDto;
import ga.backend.metro.dto.MetroResponseDto;
import ga.backend.metro.entity.Metro;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MetroMapper {
    Metro metroPostDtoToMetro(MetroRequestDto.Post post);
    Metro metroPatchDtoToMetro(MetroRequestDto.Patch patch);
    MetroResponseDto.Response metroToMetroResponseDto(Metro metro);
    List<MetroResponseDto.Response> metroToListMetroResponseDto(List<Metro> metros);
}