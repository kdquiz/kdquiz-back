package kdquiz.quiz.dto;

import lombok.*;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class QuestionUpDto {
    private Long id;
    private String content;

}
