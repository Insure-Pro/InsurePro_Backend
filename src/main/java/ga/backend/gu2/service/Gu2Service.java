package ga.backend.gu2.service;

import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.gu2.entity.Gu2;
import ga.backend.gu2.repository.Gu2Repository;
import ga.backend.metro2.service.Metro2Service;
import ga.backend.metro2.entity.Metro2;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class Gu2Service {
    private final Gu2Repository guRespository;
    private final Metro2Service metroService;

    // CREATE
    public Gu2 createGu(Gu2 gu) {
        return guRespository.save(gu);
    }

    public Gu2 createGu(String guName, Metro2 metro) {
        Gu2 gu = new Gu2();
        gu.setMetro2(metro);
        gu.setGuName(guName);
        return guRespository.save(gu);
    }

    public Gu2 createGu(String guName, long metroPk) {
        Gu2 gu = new Gu2();
        gu.setMetro2(metroService.verifiedMetro(metroPk));
        gu.setGuName(guName);
        return guRespository.save(gu);
    }

    public void createGus(List<String> guNames, long metroPk) {
        guNames.forEach(guName -> createGu(guName, metroPk));
    }

    // READ
    public Gu2 findGu(long guPk) {
        Gu2 gu = verifiedGu(guPk);
        return gu;
    }

    // 모든 내용 반환
    public List<Gu2> findGus() {
        List<Gu2> gus = guRespository.findAll();
        return gus;
    }

    // metro-pk에 해당하는 Gu 내용 반환
    public List<Gu2> findGusByMetroPk(long metroPk) {
        List<Gu2> gus = guRespository.findByMetro2_Pk(metroService.findMetro(metroPk).getPk());
        return gus;
    }

    // gu이름과 metroPk로 Gu 반환
    public Gu2 findGuByGuAndMetroPk(String guName, long metroPk) {
        Optional<Gu2> gu = guRespository.findByGuNameAndMetro2_Pk(guName, metroPk);
        return gu.orElse(null);
    }

    // gu 이름으로 Gu 반환
    public Gu2 findGuByGuName(String guName) {
        Optional<Gu2> gu = guRespository.findByGuName(guName);
        return gu.orElse(null);
    }

    // UPDATE
    public Gu2 patchGu(Gu2 gu) {
        Gu2 findGu = verifiedGu(gu.getPk());
        Optional.ofNullable(gu.getGuName()).ifPresent(findGu::setGuName);
        Optional.ofNullable(gu.getDelYn()).ifPresent(findGu::setDelYn);
        return guRespository.save(findGu);
    }

    // DELETE
    public void deleteGu(long guPk) {
        Gu2 gu = verifiedGu(guPk);
        guRespository.delete(gu);
    }

    // 검증
    public Gu2 verifiedGu(long guPk) {
        Optional<Gu2> gu = guRespository.findById(guPk);
        return gu.orElseThrow(() -> new BusinessLogicException(ExceptionCode.GU_NOT_FOUND));
    }
}
