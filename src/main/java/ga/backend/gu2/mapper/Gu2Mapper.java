package ga.backend.gu2.mapper;

import ga.backend.gu2.entity.Gu2;
import ga.backend.gu2.dto.GuRequestDto;
import ga.backend.gu2.dto.GuResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface Gu2Mapper {
    Gu2 guPostDtoToGu(GuRequestDto.Post post);
    Gu2 guPatchDtoToGu(GuRequestDto.Patch patch);
    GuResponseDto.Response guToGuResponseDto(Gu2 gu);
    List<GuResponseDto.Response> guToListGuResponseDto(List<Gu2> gu);
}