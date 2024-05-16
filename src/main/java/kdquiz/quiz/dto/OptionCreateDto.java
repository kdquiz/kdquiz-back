package kdquiz.quiz.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OptionCreateDto {
    private int time=0; //질문 시간

    private Boolean useHint=false; //힌트여부

    private int hintTime=0; //힌트 시간

    private String hintContent=""; //힌트질문

    private Boolean useAiFeedback=false; //Ai여부

    private String aiQuestion="";//ai질문

    private String commentary=""; //해설

    private int score=0; //점수

}
