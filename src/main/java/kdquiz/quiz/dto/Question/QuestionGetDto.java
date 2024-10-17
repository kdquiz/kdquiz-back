package kdquiz.quiz.dto.Question;

import kdquiz.quiz.dto.Choice.ChoiceGetDto;
import kdquiz.quiz.dto.Img.ImgGetDto;
import kdquiz.quiz.dto.Option.OptionGetDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class QuestionGetDto {
    private Long id;
    private String content;
    private Integer ord;
    private String shortAnswer;
    private Integer type;
    private OptionGetDto options;
    private List<ChoiceGetDto> choices;
    private List<ImgGetDto> uploadFileNames = new ArrayList<>();
}
