package kdquiz.quiz.dto.Quiz;

import kdquiz.quiz.dto.Question.QuestionUpdateDto;
import lombok.*;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class QuizUpdateDto {
    private String title;
}
