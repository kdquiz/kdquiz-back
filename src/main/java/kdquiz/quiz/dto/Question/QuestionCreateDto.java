package kdquiz.quiz.dto.Question;

import kdquiz.quiz.dto.Choice.ChoiceCreateDto;
import kdquiz.quiz.dto.Option.OptionCreateDto;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class QuestionCreateDto {
    private String content="";
    private OptionCreateDto options;
    private List<ChoiceCreateDto> choices;
}
