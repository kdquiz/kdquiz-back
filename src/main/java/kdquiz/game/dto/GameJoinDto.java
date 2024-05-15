package kdquiz.game.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GameJoinDto {
    private int pin;
    private String nickname;
}
