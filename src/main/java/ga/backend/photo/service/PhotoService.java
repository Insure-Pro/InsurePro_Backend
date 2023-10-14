package ga.backend.photo.service;

import ga.backend.employee.dto.EmployeeResponseDto;
import ga.backend.employee.entity.Employee;
import ga.backend.employee.mapper.EmployeeMapper;
import ga.backend.employee.service.EmployeeService;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.photo.dto.PhotoDetailResponseDto;
import ga.backend.photo.dto.PhotoResponseDto;
import ga.backend.photo.entity.Photo;
import ga.backend.photo.mapper.PhotoMapper;
import ga.backend.photo.repository.PhotoRepository;
import ga.backend.team.entity.Team;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class PhotoService {
    private final PhotoRepository photoRespository;
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    private final PhotoMapper photoMapper;

    // CREATE
    public Photo createPhoto(Photo photo, Long employeePk) {
        photo.setEmployee(employeeService.verifiedEmployeeByPk(employeePk));
        return photoRespository.save(photo);
    }


    /**
     * 자신을 제외한 팀원들의 플래너
     *
     * @param employeePk
     * @return
     */
    public List<PhotoDetailResponseDto> findTeamPhotoListByEmployee(Long employeePk) {

        Employee employee = employeeService.verifiedEmployeeByPk(employeePk);
        List<PhotoDetailResponseDto> photoDetailList = new ArrayList<>();

        if (employee.getTeam() != null) {
            for (Employee teamEmployee : employee.getTeam().getEmployees()) {
                if (teamEmployee.getPhotos().size() != 0 && !teamEmployee.equals(employee)) {
                    photoDetailList.add(employeePhotoDetail(teamEmployee));
                }
            }
        }
        return photoDetailList;
    }

    /**
     * Employee의 가장 최근 Photo(Planner)
     * @param employeePk
     * @return
     */
    public PhotoDetailResponseDto findMyPhotoByEmployee(Long employeePk) {
        Employee employee = employeeService.verifiedEmployeeByPk(employeePk);
        return employeePhotoDetail(employee);
    }

    /**
     * Employee의 Photo List
     * @param employeePk
     * @return
     */
    public List<PhotoResponseDto.Response> findMyPhotoListByEmployee(Long employeePk) {
        Employee employee = employeeService.verifiedEmployeeByPk(employeePk);
        return photoMapper.photoToPhotoListResponseDto(photoRespository.findAllByDelYnAndEmployeeOrderByCreatedAtDesc(false, employee));
    }


    public Photo findPhoto(long photoPk) {
        Photo photo = verifiedPhoto(photoPk);
        return photo;
    }

    // UPDATE
    public Photo patchPhoto(Photo photo, Long employeePk) {
        Photo findPhoto = verifiedPhoto(photo.getPk());
        if (employeePk != null) {
            Employee employee = employeeService.verifiedEmployeeByPk(employeePk);
            findPhoto.setEmployee(employee);
        }
        Optional.ofNullable(photo.getName()).ifPresent(findPhoto::setName);
        Optional.ofNullable(photo.getPhotoUrl()).ifPresent(findPhoto::setPhotoUrl);

        return photoRespository.save(findPhoto);
    }

    // DELETE
    public Photo deletePhoto(long photoPk) {
        Photo photo = verifiedPhoto(photoPk);
        photo.setDelYn(true);
        photoRespository.save(photo);
        return photo;
    }

    // 검증
    public Photo verifiedPhoto(long photoPk) {
        Optional<Photo> photo = photoRespository.findById(photoPk);
        return photo.orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMPANY_NOT_FOUND));
    }

    /**
     * Employee의 가장 최근 Photo에 대한 PhotoDetailResponseDto 생성
     *
     * @param employee
     * @return
     */
    private PhotoDetailResponseDto employeePhotoDetail(Employee employee) {
        PhotoResponseDto.Response photoResponseDto = photoMapper.photoToPhotoResponseDto(findRecentPhotoByEmployee(employee));
        EmployeeResponseDto.Response employeeReponseDto = employeeMapper.employeeToEmployeeResponseDto(employee);
        return new PhotoDetailResponseDto(employeeReponseDto, photoResponseDto);
    }

    /**
     * 직원의 가장 최신 photo
     *
     * @param employee
     * @return
     */
    private Photo findRecentPhotoByEmployee(Employee employee) {
        return photoRespository.findTopByDelYnAndEmployeeOrderByCreatedAtDesc(false, employee).orElse(null);
    }

}
