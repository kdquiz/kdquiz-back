package kdquiz.quiz.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class QuestionUpdateDto {
    private Long id;
    private String content;
    private List<ChoiceUpdateDto> choices;
    private OptionUpdateDto options;
    private Boolean imgTF;

}
