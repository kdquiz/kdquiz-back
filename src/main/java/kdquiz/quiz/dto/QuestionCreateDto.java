package kdquiz.quiz.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
    private Boolean imgTF=false;
}
