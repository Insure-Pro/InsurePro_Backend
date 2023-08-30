package ga.backend.metro.service;

import ga.backend.metro.entity.Metro;
import ga.backend.metro.repository.MetroRepository;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MetroService {
    private final MetroRepository metroRespository;

    // CREATE
    public Metro createMetro(Metro aMetro) {
        return metroRespository.save(aMetro);
    }

    // READ
    public Metro findMetro(long metroPk) {
        Metro aMetro = verifiedMetro(metroPk);
        return aMetro;
    }

    // UPDATE
    public Metro patchMetro(Metro aMetro) {
        Metro findMetro = verifiedMetro(aMetro.getPk());
        Optional.ofNullable(aMetro.getMetro()).ifPresent(findMetro::setMetro);
        Optional.ofNullable(aMetro.getDelYn()).ifPresent(findMetro::setDelYn);
        return metroRespository.save(findMetro);
    }

    // DELETE
    public void deleteMetro(long metroPk) {
        Metro aMetro = verifiedMetro(metroPk);
        metroRespository.delete(aMetro);
    }

    // 검증
    public Metro verifiedMetro(long metroPk) {
        Optional<Metro> aMetro = metroRespository.findById(metroPk);
        return aMetro.orElseThrow(() -> new BusinessLogicException(ExceptionCode.DONG_NOT_FOUND));
    }
}
