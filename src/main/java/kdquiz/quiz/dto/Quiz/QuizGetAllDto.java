package kdquiz.quiz.dto.Quiz;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class QuizGetAllDto {
    private Long id;
    private String title;
    private String type;
    private LocalDateTime create_at;
    private LocalDateTime Update_at;
}
