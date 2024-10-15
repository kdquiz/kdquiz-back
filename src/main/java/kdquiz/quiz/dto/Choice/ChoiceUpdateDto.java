package kdquiz.quiz.dto.Choice;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ChoiceUpdateDto {
    private long id;
    private String content;
    private Boolean isCorrect;
    private String shortAnswer;

}
