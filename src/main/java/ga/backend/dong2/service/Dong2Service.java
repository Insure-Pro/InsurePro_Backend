package ga.backend.dong2.service;

import ga.backend.dong2.entity.Dong2;
import ga.backend.dong2.repository.Dong2Repository;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.gu2.entity.Gu2;
import ga.backend.gu2.service.Gu2Service;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class Dong2Service {
    private final Dong2Repository dongRespository;
    private final Gu2Service guService;

    // CREATE
    public Dong2 createDong(Dong2 dong) {
        return dongRespository.save(dong);
    }

    public Dong2 createDong(String dongName, Gu2 gu) {
        Dong2 dong = new Dong2();
        dong.setGu2(gu);
        dong.setDongName(dongName);
        return dongRespository.save(dong);
    }

    public Dong2 createDong(String dongName, long guPk) {
        Dong2 dong = new Dong2();
        dong.setGu2(guService.verifiedGu(guPk));
        dong.setDongName(dongName);
        return dongRespository.save(dong);
    }

    public void createDongs(List<String> dongNames, long guPk) {
        dongNames.forEach(dongName -> createDong(dongName, guPk));
    }

    // READ
    public Dong2 findDong(long dongPk) {
        Dong2 dong = verifiedDong(dongPk);
        return dong;
    }

    // 구에 해당하는 Dong 반환
    public List<Dong2> findDongs() {
        List<Dong2> dongs = dongRespository.findAll();
        return dongs;
    }

    // gu-pk에 해당하는 Dong 반환
    public List<Dong2> findDongs(long guPk) {
        List<Dong2> dongs = dongRespository.findByGu2_Pk(guService.findGu(guPk).getPk());
        return dongs;
    }

    // dong이름과 gu-pk로 Dong 반환
    public Dong2 findDongByDongAndGuPk(String dongName, long guPk) {
        Optional<Dong2> dong = dongRespository.findByDongNameAndGu2_Pk(dongName, guPk);
        return dong.orElse(null);
    }

    // dong이름으로 Dong 반환
    public Dong2 findDongByDongName(String dongName) {
        Optional<Dong2> dong = dongRespository.findByDongName(dongName);
        return dong.orElse(null);
    }

    public Dong2 findDongByDongNameAndGu(String dongName, Gu2 gu2) {
        Optional<Dong2> dong = dongRespository.findByDongNameAndGu2(dongName, gu2);
        return dong.orElse(null);
    }

    // UPDATE
    public Dong2 patchDong(Dong2 dong) {
        Dong2 findDong = verifiedDong(dong.getPk());
        Optional.ofNullable(dong.getDongName()).ifPresent(findDong::setDongName);
        Optional.ofNullable(dong.getDelYn()).ifPresent(findDong::setDelYn);
        return dongRespository.save(findDong);
    }

    // DELETE
    public void deleteDong(long dongPk) {
        Dong2 dong = verifiedDong(dongPk);
        dongRespository.delete(dong);
    }

    public void deleteDong(Dong2 dong2) {
        dongRespository.delete(dong2);
    }

    // 검증
    public Dong2 verifiedDong(long dongPk) {
        Optional<Dong2> dong = dongRespository.findById(dongPk);
        return dong.orElseThrow(() -> new BusinessLogicException(ExceptionCode.DONG_NOT_FOUND));
    }
}
