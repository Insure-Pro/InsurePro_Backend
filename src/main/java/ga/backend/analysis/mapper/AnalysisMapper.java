//package ga.backend.analysis.mapper;
//
//import ga.backend.analysis.dto.AnalysisRequestDto;
//import ga.backend.analysis.dto.AnalysisResponseDto;
//import ga.backend.analysis.entity.Analysis;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//import java.util.List;
//
//@Mapper(componentModel = "spring")
//public interface AnalysisMapper {
//
//    Analysis analysisPostDtoToAnalysis(AnalysisRequestDto.Post post);
//    Analysis analysisPatchDtoToAnalysis(AnalysisRequestDto.Patch patch);
//    AnalysisResponseDto.Response analysisToAnalysisResponseDto(Analysis analysis);
//
//}