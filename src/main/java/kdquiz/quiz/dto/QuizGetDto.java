package kdquiz.quiz.dto;

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
