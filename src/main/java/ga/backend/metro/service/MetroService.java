package ga.backend.metro.service;

import ga.backend.metro.entity.Metro;
import ga.backend.metro.repository.MetroRepository;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MetroService {
    private final MetroRepository metroRespository;

    // CREATE
    public Metro createMetro(Metro metro) {
        return metroRespository.save(metro);
    }

    public Metro createMetro(String metroName) {
        Metro metro = new Metro();
        metro.setMetro(metroName);
        return metroRespository.save(metro);
    }

    // READ
    public Metro findMetro(long metroPk) {
        Metro metro = verifiedMetro(metroPk);
        return metro;
    }
    
    // 모든 내용 반환
    public List<Metro> findMetros() {
        List<Metro> metros = metroRespository.findAll();
        return metros;
    }

    // metro 이름으로 Metro 반환
    public Metro findMetroByMetro(String metroName) {
        Optional<Metro> metro = metroRespository.findByMetro(metroName);
        return metro.orElse(null);
    }

    // UPDATE
    public Metro patchMetro(Metro metro) {
        Metro findMetro = verifiedMetro(metro.getPk());
        Optional.ofNullable(metro.getMetro()).ifPresent(findMetro::setMetro);
        Optional.ofNullable(metro.getDelYn()).ifPresent(findMetro::setDelYn);
        return metroRespository.save(findMetro);
    }

    // DELETE
    public void deleteMetro(long metroPk) {
        Metro metro = verifiedMetro(metroPk);
        metroRespository.delete(metro);
    }

    // 검증
    public Metro verifiedMetro(long metroPk) {
        Optional<Metro> metro = metroRespository.findById(metroPk);
        return metro.orElseThrow(() -> new BusinessLogicException(ExceptionCode.METRO_NOT_FOUND));
    }
}
