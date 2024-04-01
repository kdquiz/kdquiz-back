package kdquiz.quiz.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ChoicesCreateDto {
    private String content;
    private Boolean isCorrect;
}
