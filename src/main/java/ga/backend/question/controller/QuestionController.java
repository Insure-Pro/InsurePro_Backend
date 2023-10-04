package ga.backend.question.controller;

import com.nimbusds.jose.shaded.json.JSONObject;
import ga.backend.employee.dto.EmployeeResponseDto;
import ga.backend.employee.entity.Employee;
import ga.backend.employee.mapper.EmployeeMapper;
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
    private final EmployeeMapper employeeMapper;

    // CREATE
    @PostMapping
    public ResponseEntity postQuestion(@Valid @RequestBody QuestionRequestDto.Post post) {
        Question question = questionService.createQuestion(questionMapper.questionPostDtoToQuestion(post));
        QuestionResponseDto.Response response = questionMapper.questionToQuestionResponseDto(question);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping
    public ResponseEntity getQuestionList (@Positive @RequestParam(value = "pk", required = false) Long questionPk,
                                       @RequestParam(value = "name", required = false) String questionName) {
        Question question = questionService.findQuestion(questionPk);

        // 응답
        QuestionResponseDto.Response questionResponse = questionMapper.questionToQuestionResponseDto(question);
        JSONObject response = new JSONObject();
        response.put("question", questionResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // UPDATE
    @PatchMapping("/{question-pk}")
    public ResponseEntity patchQuestion(@Positive @PathVariable("question-pk") long questionPk,
                                        @Valid @RequestBody QuestionRequestDto.Patch patch) {
        patch.setPk(questionPk);
        Question question = questionService.patchQuestion(questionMapper.questionPatchDtoToQuestion(patch));
        QuestionResponseDto.Response response = questionMapper.questionToQuestionResponseDto(question);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/{question-pk}")
    public ResponseEntity deleteQuestion(@Positive @PathVariable("question-pk") long questionPk) {
        questionService.deleteQuestion(questionPk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
