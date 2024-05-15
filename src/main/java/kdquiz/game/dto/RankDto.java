package kdquiz.game.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RankDto {
    private long id;
    private int score;
    private Integer rank;
}
