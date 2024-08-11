package ga.backend.question.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ga.backend.question.dto.QuestionSlack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ga.backend.employee.mapper.EmployeeMapper;
import ga.backend.employee.service.EmployeeService;
import ga.backend.question.dto.QuestionResponseDto;
import ga.backend.question.entity.Question;
import ga.backend.question.mapper.QuestionMapper;
import ga.backend.question.service.QuestionService;
import ga.backend.util.Version;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(Version.currentUrl + "/questions")
@Validated
@AllArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    private final QuestionMapper questionMapper;
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
    private final ObjectMapper objectMapper;

    // CREATE
    @PostMapping(consumes="multipart/form-data")
    public ResponseEntity postQuestion(@RequestPart(value = "content") String content,
                                       @RequestPart(value = "image", required = false) MultipartFile photo) throws JsonProcessingException {
        // question 생성
        Question question = questionService.createQuestion(content, photo);

        // Response
        QuestionResponseDto.Response response = questionMapper.questionToQuestionResponseDto(question);

        // 로그 기록
        QuestionSlack.Response slackResponse = questionMapper.questionResponseDtoToQuestionSlackResponseDto(response);
        logger.info("!! 문의사항 발생 !!\n```\n{}\n```\n", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(slackResponse));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping
    public ResponseEntity getQuestionList () {

        // 모든 question 찾기
        List<Question> questions = questionService.findQuestions();

        // 응답
        List<QuestionResponseDto.Response> response = questionMapper.questionToQuestionListResponseDto(questions);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // DELETE
    @DeleteMapping("/{question-pk}")
    public ResponseEntity deleteQuestion(@Positive @PathVariable("question-pk") long questionPk) {
        questionService.deleteQuestion(questionPk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
