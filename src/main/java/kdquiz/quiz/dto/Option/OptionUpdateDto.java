package kdquiz.quiz.dto.Option;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OptionUpdateDto {
    private Integer time; //시간
    private Boolean useHint; //힌트여부

    private Integer hintTime; //힌트 시간

    private String hintContent; //힌트질문

    private Boolean useAiFeedback; //Ai여부

    private String aiQuestion;//ai질문

    private String commentary; //해설

    private int score; //점수


}
