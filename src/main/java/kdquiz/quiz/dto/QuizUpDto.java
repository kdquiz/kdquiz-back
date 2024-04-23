package kdquiz.quiz.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class QuizUpDto {
    private List<QuestionUpDto> questions;
}
