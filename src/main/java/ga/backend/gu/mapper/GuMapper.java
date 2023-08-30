package ga.backend.gu.mapper;

import ga.backend.gu.dto.GuRequestDto;
import ga.backend.gu.dto.GuResponseDto;
import ga.backend.gu.entity.Gu;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GuMapper {
    Gu guPostDtoToGu(GuRequestDto.Post post);
    Gu guPatchDtoToGu(GuRequestDto.Patch patch);
    GuResponseDto.Response guToGuResponseDto(Gu gu);
}