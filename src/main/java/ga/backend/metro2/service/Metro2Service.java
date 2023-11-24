package ga.backend.metro2.service;

import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.metro2.entity.Metro2;
import ga.backend.metro2.repository.Metro2Repository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class Metro2Service {
    private final Metro2Repository metroRespository;

    // CREATE
    public Metro2 createMetro(Metro2 metro) {
        return metroRespository.save(metro);
    }

    public Metro2 createMetro(String metroName) {
        Metro2 metro = new Metro2();
        metro.setMetroName(metroName);
        return metroRespository.save(metro);
    }

    // READ
    public Metro2 findMetro(long metroPk) {
        Metro2 metro = verifiedMetro(metroPk);
        return metro;
    }
    
    // 모든 내용 반환
    public List<Metro2> findMetros() {
        List<Metro2> metros = metroRespository.findAll();
        return metros;
    }

    // metro 이름으로 Metro 반환
    public Metro2 findMetroByMetroName(String metroName) {
        Optional<Metro2> metro = metroRespository.findByMetroName(metroName);
        return metro.orElse(null);
    }

    // UPDATE
    public Metro2 patchMetro(Metro2 metro) {
        Metro2 findMetro = verifiedMetro(metro.getPk());
        Optional.ofNullable(metro.getMetroName()).ifPresent(findMetro::setMetroName);
        Optional.ofNullable(metro.getDelYn()).ifPresent(findMetro::setDelYn);
        return metroRespository.save(findMetro);
    }

    // DELETE
    public void deleteMetro(long metroPk) {
        Metro2 metro = verifiedMetro(metroPk);
        metroRespository.delete(metro);
    }

    // 검증
    public Metro2 verifiedMetro(long metroPk) {
        Optional<Metro2> metro = metroRespository.findById(metroPk);
        return metro.orElseThrow(() -> new BusinessLogicException(ExceptionCode.METRO_NOT_FOUND));
    }
}
