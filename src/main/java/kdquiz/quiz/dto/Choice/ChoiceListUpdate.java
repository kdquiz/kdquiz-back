package kdquiz.quiz.dto.Choice;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ChoiceListUpdate {
    private List<ChoiceUpdateDto> choices;
}
