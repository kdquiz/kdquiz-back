package kdquiz.users.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SignUpDto {
    private String email;
    private String password;
    private String nickname;
}
