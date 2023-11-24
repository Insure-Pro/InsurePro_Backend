package ga.backend.metro2.mapper;

import ga.backend.metro2.dto.MetroRequestDto;
import ga.backend.metro2.dto.MetroResponseDto;
import ga.backend.metro2.entity.Metro2;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface Metro2Mapper {
    Metro2 metroPostDtoToMetro(MetroRequestDto.Post post);
    Metro2 metroPatchDtoToMetro(MetroRequestDto.Patch patch);
    MetroResponseDto.Response metroToMetroResponseDto(Metro2 metro);
    List<MetroResponseDto.Response> metroToListMetroResponseDto(List<Metro2> metros);
}