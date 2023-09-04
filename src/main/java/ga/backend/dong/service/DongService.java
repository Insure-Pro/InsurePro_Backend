package ga.backend.dong.service;

import ga.backend.dong.entity.Dong;
import ga.backend.dong.repository.DongRepository;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.gu.entity.Gu;
import ga.backend.gu.service.GuService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DongService {
    private final DongRepository dongRespository;
    private final GuService guService;

    // CREATE
    public Dong createDong(Dong dong) {
        return dongRespository.save(dong);
    }

    public Dong createDong(String dongName, Gu gu) {
        Dong dong = new Dong();
        dong.setGu(gu);
        dong.setDong(dongName);
        return dongRespository.save(dong);
    }

    public Dong createDong(String dongName, long guPk) {
        Dong dong = new Dong();
        dong.setGu(guService.verifiedGu(guPk));
        dong.setDong(dongName);
        return dongRespository.save(dong);
    }

    public void createDongs(List<String> dongNames, long guPk) {
        dongNames.forEach(dongName -> createDong(dongName, guPk));
    }

    // READ
    public Dong findDong(long dongPk) {
        Dong dong = verifiedDong(dongPk);
        return dong;
    }

    // 구에 해당하는 Dong 반환
    public List<Dong> findDongs() {
        List<Dong> dongs = dongRespository.findAll();
        return dongs;
    }

    // gu-pk에 해당하는 Dong 반환
    public List<Dong> findDongs(long guPk) {
        List<Dong> dongs = dongRespository.findByGu_Pk(guService.findGu(guPk).getPk());
        return dongs;
    }

    // dong이름과 gu-pk로 Dong 반환
    public Dong findDongByDongAndGuPk(String dongName, long guPk) {
        Optional<Dong> dong = dongRespository.findByDongAndGu_Pk(dongName, guPk);
        return dong.orElse(null);
    }

    // dong이름으로 Dong 반환
    public Dong findDongByDong(String dongName) {
        Optional<Dong> dong = dongRespository.findByDong(dongName);
        return dong.orElse(null);
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
