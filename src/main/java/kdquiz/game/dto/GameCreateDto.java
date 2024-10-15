package kdquiz.game.dto;

import kdquiz.quiz.dto.Question.QuestionGetDto;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GameCreateDto {
    private Long id;
    private String title;
    private String type;
    private List<QuestionGetDto> questions;
    private String pin;
}
