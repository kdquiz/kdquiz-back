package kdquiz.participants.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ParticipantsDto {
    private int pin;
    private String nickname;
}
