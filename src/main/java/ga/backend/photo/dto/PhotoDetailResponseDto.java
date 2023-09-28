package ga.backend.photo.dto;

import ga.backend.employee.dto.EmployeeResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Setter
@Getter
public class PhotoDetailResponseDto {
    private EmployeeResponseDto.Response employee;
    private PhotoResponseDto.Response photo;
}
