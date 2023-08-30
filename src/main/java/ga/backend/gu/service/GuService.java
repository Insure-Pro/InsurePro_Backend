package ga.backend.gu.service;

import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.gu.entity.Gu;
import ga.backend.gu.repository.GuRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GuService {
    private final GuRepository guRespository;

    // CREATE
    public Gu createGu(Gu gu) {
        return guRespository.save(gu);
    }

    // READ
    public Gu findGu(long guPk) {
        Gu gu = verifiedGu(guPk);
        return gu;
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
        return gu.orElseThrow(() -> new BusinessLogicException(ExceptionCode.DONG_NOT_FOUND));
    }
}
