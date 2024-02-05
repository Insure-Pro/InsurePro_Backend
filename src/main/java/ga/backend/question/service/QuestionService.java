package ga.backend.question.service;


import ga.backend.employee.entity.Employee;
import ga.backend.employee.repository.EmployeeRepository;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.question.entity.Question;
import ga.backend.question.repository.QuestionRepository;
import ga.backend.s3.service.ImageService;
import ga.backend.util.FindEmployee;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRespository;
    private final ImageService imageService;
    private final FindEmployee findEmployee;

    // CREATE
    public Question createQuestion(String content, MultipartFile photo) {
        // 문의사항 생성
        Question question = new Question();
        question.setContent(content);

        // 직원 정보 조회
        Employee employee = findEmployee.getLoginEmployeeByToken();
        question.setEmployee(employee);

        // 이미지 업로드
        if (photo != null) {
            question.setImageUrl(imageService.updateImage(photo, "photo", "imageUrl"));
        }

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
        return question.orElseThrow(() -> new BusinessLogicException(ExceptionCode.QUESTION_NOT_FOUND));
    }
}
