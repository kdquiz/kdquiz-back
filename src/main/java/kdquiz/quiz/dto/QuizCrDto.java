package kdquiz.quiz.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class QuizCrDto {
    private List<QuestionCrDto> questions;
}
