package kdquiz.quiz.dto.Quiz;

import kdquiz.quiz.dto.Question.QuestionGetDto;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class QuizGetDto {
    private Long id;
    private String title;
    private String type;
    private List<QuestionGetDto> questions;
}
