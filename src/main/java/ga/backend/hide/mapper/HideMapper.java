package ga.backend.hide.mapper;

import ga.backend.hide.dto.HideRequestDto;
import ga.backend.hide.dto.HideResponseDto;
import ga.backend.hide.entity.Hide;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HideMapper {
    Hide hidePostDtoToHide(HideRequestDto.Post post);
    Hide hidePatchDtoToHide(HideRequestDto.Patch patch);
    HideResponseDto.Response HideToHideResponseDto(Hide hide);
}