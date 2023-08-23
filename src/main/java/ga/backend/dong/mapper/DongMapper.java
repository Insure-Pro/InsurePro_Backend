package ga.backend.dong.mapper;

import ga.backend.dong.dto.DongRequestDto;
import ga.backend.dong.dto.DongResponseDto;
import ga.backend.dong.entity.Dong;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DongMapper {
    Dong dongPostDtoToDong(DongRequestDto.Post post);
    Dong dongPatchDtoToDong(DongRequestDto.Patch patch);
    DongResponseDto.Response dongToDongResponseDto(Dong dong);
}