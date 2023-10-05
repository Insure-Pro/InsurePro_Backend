package ga.backend.question.mapper;

import ga.backend.question.dto.QuestionRequestDto;
import ga.backend.question.dto.QuestionResponseDto;
import ga.backend.question.entity.Question;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    Question questionPostDtoToQuestion(QuestionRequestDto.Post post);
    Question questionPatchDtoToQuestion(QuestionRequestDto.Patch patch);
    QuestionResponseDto.Response questionToQuestionResponseDto(Question question);
    List<QuestionResponseDto.Response> questionToQuestionListResponseDto(List<Question> questions);
}