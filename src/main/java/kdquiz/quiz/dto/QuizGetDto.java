package kdquiz.quiz.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class QuizGetDto {
    private Long id;
    private String title;
    private String type;
}
