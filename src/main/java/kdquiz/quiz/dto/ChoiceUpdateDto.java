package kdquiz.quiz.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ChoiceUpdateDto {
    private String content;
    private Boolean isCorrect;
    private Long id;
}
