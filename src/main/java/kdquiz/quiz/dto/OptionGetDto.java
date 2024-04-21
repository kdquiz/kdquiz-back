package kdquiz.quiz.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OptionGetDto {
    private Long id;

    private int time;

    private Boolean useHint; //힌트여부

    private int hintTime; //힌트 시간

    private String hintContent; //힌트질문

    private Boolean useAiFeedback; //Ai여부

    private String aiQuestion;//ai질문

    private String commentary; //해설

    private int score; //점수
}
