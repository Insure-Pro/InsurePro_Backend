package ga.backend.question.service;


import ga.backend.employee.entity.Employee;
import ga.backend.employee.repository.EmployeeRepository;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.question.entity.Question;
import ga.backend.question.repository.QuestionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRespository;
    private final EmployeeRepository employeeRepository;

    // CREATE
    public Question createQuestion(Question question, Employee employee) {
        question.setEmployee(employee);
        return questionRespository.save(question);
    }

    // READ
    public List<Question> findQuestions() {
        return questionRespository.findAll();
    }

    // DELETE
    public void deleteQuestion(long questionPk) {
        Question question = verifiedQuestion(questionPk);
        questionRespository.delete(question);
    }


    // 검증
    public Question verifiedQuestion(long questionPk) {
        Optional<Question> question = questionRespository.findById(questionPk);
        return question.orElseThrow(() -> new BusinessLogicException(ExceptionCode.TEAM_NOT_FOUND));
    }
}
