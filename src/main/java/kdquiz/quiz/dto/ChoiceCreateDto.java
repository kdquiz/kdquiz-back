package kdquiz.quiz.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ChoiceCreateDto {
    private String content="";
    private Boolean isCorrect=false;
    private String shortAnswer="";
}
