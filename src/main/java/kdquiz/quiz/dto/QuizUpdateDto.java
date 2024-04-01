package kdquiz.quiz.dto;

import lombok.*;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class QuizUpdateDto {
    private String title;
    private String type;
    private List<QuestionUpdateDto> questions;
}
