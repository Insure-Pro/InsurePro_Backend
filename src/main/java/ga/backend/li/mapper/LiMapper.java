package ga.backend.li.mapper;

import ga.backend.li.dto.LiRequestDto;
import ga.backend.li.dto.LiResponseDto;
import ga.backend.li.entity.Li;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LiMapper {
    Li liPostDtoToLi(LiRequestDto.Post post);
    Li liPatchDtoToLi(LiRequestDto.Patch patch);
    LiResponseDto.Response liToLiResponseDto(Li li);
    List<LiResponseDto.Response> liToListLiResponseDto(List<Li> li);
}