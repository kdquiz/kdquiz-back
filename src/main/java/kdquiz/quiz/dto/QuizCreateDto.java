package kdquiz.quiz.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class QuizCreateDto {
    private String title;
    private String type;
    private List<QuestionCreateDto> questions;
}
