package kdquiz.quiz.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ChoiceGetDto {
    private Long id;
    private String content;
    private Boolean isCorrect;
    private String shortAnswer;
}
