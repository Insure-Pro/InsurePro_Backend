package ga.backend.dong.mapper;

import ga.backend.dong.dto.DongRequestDto;
import ga.backend.dong.dto.DongResponseDto;
import ga.backend.dong.entity.Dong;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DongMapper {
    Dong dongPostDtoToDong(DongRequestDto.Post post);
    Dong dongPatchDtoToDong(DongRequestDto.Patch patch);
    DongResponseDto.Response dongToDongResponseDto(Dong dong);
    List<DongResponseDto.Response> dongToListDongResponseDto(List<Dong> dong);
}