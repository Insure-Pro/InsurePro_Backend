package ga.backend.progress.mapper;

import ga.backend.progress.dto.ProgressRequestDto;
import ga.backend.progress.dto.ProgressResponseDto;
import ga.backend.progress.entity.Progress;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProgressMapper {
    Progress progressPostDtoToProgress(ProgressRequestDto.Post post);
    Progress progressPatchDtoToProgress(ProgressRequestDto.Patch patch);
    ProgressResponseDto.Response progressToProgressResponseDto(Progress progress);
}