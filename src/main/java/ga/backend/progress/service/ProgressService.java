package ga.backend.progress.service;

import ga.backend.progress.entity.Progress;
import ga.backend.progress.repository.ProgressRepository;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProgressService {
    private final ProgressRepository progressRespository;

    // CREATE
    public Progress createProgress(Progress progress) {
        return progressRespository.save(progress);
    }

    // READ
    public Progress findProgress(long progressPk) {
        Progress progress = verifiedProgress(progressPk);
        return progress;
    }

    // UPDATE
    public Progress patchProgress(Progress progress) {
        Progress findProgress = verifiedProgress(progress.getPk());
        Optional.ofNullable(progress.getOptionName()).ifPresent(findProgress::setOptionName);

        return progressRespository.save(findProgress);
    }

    // DELETE
    public void deleteProgress(long progressPk) {
        Progress progress = verifiedProgress(progressPk);
        progressRespository.delete(progress);
    }

    // 검증
    public Progress verifiedProgress(long progressPk) {
        Optional<Progress> progress = progressRespository.findById(progressPk);
        return progress.orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMPANY_NOT_FOUND));
    }
}
