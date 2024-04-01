package kdquiz.quiz.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class QuestionCreateDto {
    private String content;
    private int score;
    private List<ChoicesCreateDto> choices;
}
