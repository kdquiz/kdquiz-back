package kdquiz.quiz.dto;

import lombok.*;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class QuestionGetDto {
    private Long id;
    private String content;
    private OptionGetDto options;
    private List<ChoiceGetDto> choices;
    private String imgUrl;
}
