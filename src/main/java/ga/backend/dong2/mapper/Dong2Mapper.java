package ga.backend.dong2.mapper;

import ga.backend.dong2.dto.DongRequestDto;
import ga.backend.dong2.dto.DongResponseDto;
import ga.backend.dong2.entity.Dong2;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface Dong2Mapper {
    Dong2 dongPostDtoToDong(DongRequestDto.Post post);
    Dong2 dongPatchDtoToDong(DongRequestDto.Patch patch);
    DongResponseDto.Response dongToDongResponseDto(Dong2 dong);
    List<DongResponseDto.Response> dongToListDongResponseDto(List<Dong2> dong);
}