package kdquiz.quiz.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class QuestionUpdateDto {
    private String content;
    private int score;
    private List<ChoiceUpdateDto> choices;
    private Long id;
}
