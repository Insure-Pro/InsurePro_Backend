package ga.backend.performance.service;

import ga.backend.performance.entity.Performance;
import ga.backend.performance.repository.PerformanceRepository;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PerformanceService {
    private final PerformanceRepository performanceRespository;

    // CREATE
    public Performance createPerformance(Performance performance) {
        return performanceRespository.save(performance);
    }

    // READ
    public Performance findPerformance(long performancePk) {
        Performance performance = verifiedPerformance(performancePk);
        return performance;
    }

    // UPDATE
    public Performance patchPerformance(Performance performance) {
        Performance findPerformance = verifiedPerformance(performance.getPk());
        Optional.ofNullable(performance.getDate()).ifPresent(findPerformance::setDate);
        Optional.ofNullable(performance.getStartTm()).ifPresent(findPerformance::setStartTm);
        Optional.ofNullable(performance.getFinishTm()).ifPresent(findPerformance::setFinishTm);
        if(performance.getTa() != 0) findPerformance.setTa(performance.getTa());
        if(performance.getIntroduction() != 0) findPerformance.setIntroduction(performance.getIntroduction());
        if(performance.getRp() != 0) findPerformance.setRp(performance.getRp());
        if(performance.getApc() != 0) findPerformance.setApc(performance.getApc());
        if(performance.getContractNm() != 0) findPerformance.setContractNm(performance.getContractNm());

        return performanceRespository.save(findPerformance);
    }

    // DELETE
    public void deletePerformance(long performancePk) {
        Performance performance = verifiedPerformance(performancePk);
        performanceRespository.delete(performance);
    }

    // 검증
    public Performance verifiedPerformance(long performancePk) {
        Optional<Performance> performance = performanceRespository.findById(performancePk);
        return performance.orElseThrow(() -> new BusinessLogicException(ExceptionCode.PERFORMANCE_NOT_FOUND));
    }
}
