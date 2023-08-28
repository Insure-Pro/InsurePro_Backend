package ga.backend.performance.mapper;

import ga.backend.performance.dto.PerformanceRequestDto;
import ga.backend.performance.dto.PerformanceResponseDto;
import ga.backend.performance.entity.Performance;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-28T15:44:09+0900",
    comments = "version: 1.5.1.Final, compiler: javac, environment: Java 11.0.16.1 (Oracle Corporation)"
)
@Component
public class PerformanceMapperImpl implements PerformanceMapper {

    @Override
    public Performance performancePostDtoToPerformance(PerformanceRequestDto.Post post) {
        if ( post == null ) {
            return null;
        }

        Performance performance = new Performance();

        performance.setPk( post.getPk() );
        performance.setDate( post.getDate() );
        performance.setStartTm( post.getStartTm() );
        performance.setFinishTm( post.getFinishTm() );
        performance.setTa( post.getTa() );
        performance.setIntroduction( post.getIntroduction() );
        performance.setRp( post.getRp() );
        performance.setApc( post.getApc() );
        performance.setContractNm( post.getContractNm() );

        return performance;
    }

    @Override
    public Performance performancePatchDtoToPerformance(PerformanceRequestDto.Patch patch) {
        if ( patch == null ) {
            return null;
        }

        Performance performance = new Performance();

        performance.setPk( patch.getPk() );
        performance.setDate( patch.getDate() );
        performance.setStartTm( patch.getStartTm() );
        performance.setFinishTm( patch.getFinishTm() );
        performance.setTa( patch.getTa() );
        performance.setIntroduction( patch.getIntroduction() );
        performance.setRp( patch.getRp() );
        performance.setApc( patch.getApc() );
        performance.setContractNm( patch.getContractNm() );

        return performance;
    }

    @Override
    public PerformanceResponseDto.Response performanceToPerformanceResponseDto(Performance performance) {
        if ( performance == null ) {
            return null;
        }

        Long pk = null;
        LocalDate date = null;
        LocalTime startTm = null;
        LocalTime finishTm = null;
        int ta = 0;
        int introduction = 0;
        int rp = 0;
        int apc = 0;
        int contractNm = 0;
        LocalDateTime createdAt = null;
        LocalDateTime modifiedAt = null;

        pk = performance.getPk();
        date = performance.getDate();
        startTm = performance.getStartTm();
        finishTm = performance.getFinishTm();
        ta = performance.getTa();
        introduction = performance.getIntroduction();
        rp = performance.getRp();
        apc = performance.getApc();
        contractNm = performance.getContractNm();
        createdAt = performance.getCreatedAt();
        modifiedAt = performance.getModifiedAt();

        PerformanceResponseDto.Response response = new PerformanceResponseDto.Response( pk, date, startTm, finishTm, ta, introduction, rp, apc, contractNm, createdAt, modifiedAt );

        return response;
    }
}
