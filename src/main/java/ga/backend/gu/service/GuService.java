package ga.backend.gu.service;

import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.gu.entity.Gu;
import ga.backend.gu.repository.GuRepository;
import ga.backend.metro.service.MetroService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GuService {
    private final GuRepository guRespository;
    private final MetroService metroService;

    // CREATE
    public Gu createGu(Gu gu) {
        return guRespository.save(gu);
    }

    public Gu createGu(String guName, long metroPk) {
        Gu gu = new Gu();
        gu.setMetro(metroService.verifiedMetro(metroPk));
        gu.setGu(guName);
        return guRespository.save(gu);
    }

    public void createGus(List<String> guNames, long metroPk) {
        guNames.forEach(guName -> createGu(guName, metroPk));
    }

    // READ
    public Gu findGu(long guPk) {
        Gu gu = verifiedGu(guPk);
        return gu;
    }

    // 모든 내용 반환
    public List<Gu> findGus() {
        List<Gu> gus = guRespository.findAll();
        return gus;
    }

    // metro-pk에 해당하는 Gu 내용 반환
    public List<Gu> findGusByMetroPk(long metroPk) {
        List<Gu> gus = guRespository.findByMetro_Pk(metroPk);
        return gus;
    }

    // gu이름과 metroPk로 Gu 반환
    public Gu findGuByGuAndMetroPk(String guName, long metroPk) {
        Optional<Gu> gu = guRespository.findByGuAndMetro_Pk(guName, metroPk);
        return gu.orElse(null);
    }

    // gu 이름으로 Gu 반환
    public Gu findGuByGu(String guName) {
        Optional<Gu> gu = guRespository.findByGu(guName);
        return gu.orElse(null);
    }

    // UPDATE
    public Gu patchGu(Gu gu) {
        Gu findGu = verifiedGu(gu.getPk());
        Optional.ofNullable(gu.getGu()).ifPresent(findGu::setGu);
        Optional.ofNullable(gu.getDelYn()).ifPresent(findGu::setDelYn);
        return guRespository.save(findGu);
    }

    // DELETE
    public void deleteGu(long guPk) {
        Gu gu = verifiedGu(guPk);
        guRespository.delete(gu);
    }

    // 검증
    public Gu verifiedGu(long guPk) {
        Optional<Gu> gu = guRespository.findById(guPk);
        return gu.orElseThrow(() -> new BusinessLogicException(ExceptionCode.GU_NOT_FOUND));
    }
}
