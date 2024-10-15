package kdquiz.quiz.dto.Question;

import kdquiz.quiz.dto.Choice.ChoiceUpdateDto;
import kdquiz.quiz.dto.Option.OptionUpdateDto;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class QuestionUpdateDto {
    private String content;
    private OptionUpdateDto options;
    private List<ChoiceUpdateDto> choices;
    private List<MultipartFile> files = new ArrayList<>();
    private List<String> uploadFileNames = new ArrayList<>();
}
