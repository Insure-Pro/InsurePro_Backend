package ga.backend.dong.service;

import ga.backend.dong.entity.Dong;
import ga.backend.dong.repository.DongRepository;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DongService {
    private final DongRepository dongRespository;

    // CREATE
    public Dong createDong(Dong dong) {
        return dongRespository.save(dong);
    }

    // READ
    public Dong findDong(long dongPk) {
        Dong dong = verifiedDong(dongPk);
        return dong;
    }

    // UPDATE
    public Dong patchDong(Dong dong) {
        Dong findDong = verifiedDong(dong.getPk());
        Optional.ofNullable(dong.getDong()).ifPresent(findDong::setDong);
        Optional.ofNullable(dong.getDelYn()).ifPresent(findDong::setDelYn);
        return dongRespository.save(findDong);
    }

    // DELETE
    public void deleteDong(long dongPk) {
        Dong dong = verifiedDong(dongPk);
        dongRespository.delete(dong);
    }

    // 검증
    public Dong verifiedDong(long dongPk) {
        Optional<Dong> dong = dongRespository.findById(dongPk);
        return dong.orElseThrow(() -> new BusinessLogicException(ExceptionCode.DONG_NOT_FOUND));
    }
}
