package kdquiz.quiz.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class QuizGetAllDto {
    private Long id;
    private String title;
    private String type;
}
