package ga.backend.question.controller;

import com.nimbusds.jose.shaded.json.JSONObject;
import ga.backend.employee.dto.EmployeeResponseDto;
import ga.backend.employee.entity.Employee;
import ga.backend.employee.mapper.EmployeeMapper;
import ga.backend.employee.service.EmployeeService;
import ga.backend.question.dto.QuestionRequestDto;
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

import javax.validation.Valid;
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

    // CREATE
    @PostMapping
    public ResponseEntity postQuestion(@Valid @RequestBody QuestionRequestDto.Post post) {
        // 직원 정보 조회
        Employee employee = employeeService.findEmployeeByToken();

        // question 생성
        Question question = questionService.createQuestion(questionMapper.questionPostDtoToQuestion(post), employee);

        // Response
        QuestionResponseDto.Response response = questionMapper.questionToQuestionResponseDto(question);

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
