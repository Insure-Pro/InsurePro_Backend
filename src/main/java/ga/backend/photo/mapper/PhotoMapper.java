package ga.backend.photo.mapper;

import ga.backend.photo.dto.PhotoRequestDto;
import ga.backend.photo.dto.PhotoResponseDto;
import ga.backend.photo.entity.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PhotoMapper {

    Photo photoPostDtoToPhoto(PhotoRequestDto.Post post);

    Photo photoPatchDtoToPhoto(PhotoRequestDto.Patch patch);
    PhotoResponseDto.Response photoToPhotoResponseDto(Photo photo);
    List<PhotoResponseDto.Response> photoToPhotoListResponseDto(List<Photo> photos);

}