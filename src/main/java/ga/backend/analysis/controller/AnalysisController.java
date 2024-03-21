package ga.backend.analysis.controller;

import ga.backend.analysis.dto.AnalysisResponseDto;
import ga.backend.analysis.entity.Analysis;
import ga.backend.analysis.mapper.AnalysisMapper;
import ga.backend.analysis.service.AnalysisService;
import ga.backend.customer.entity.CustomerTType;
import ga.backend.util.Version;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(Version.currentUrl + "/analysis")
@Validated
@AllArgsConstructor
public class AnalysisController {
    private final AnalysisService analysisService;
    private final AnalysisMapper analysisMapper;

    @GetMapping
    public ResponseEntity getCompanyList(@RequestParam("date") String date,
                                         @RequestParam("customerType") CustomerTType customerType) {
        System.out.println(customerType.getValue());
        Analysis analysis = analysisService.findAnalysis(LocalDate.parse(date), customerType);
        AnalysisResponseDto.Response response = analysisMapper.analysisToAnalysisResponseDto(analysis);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}