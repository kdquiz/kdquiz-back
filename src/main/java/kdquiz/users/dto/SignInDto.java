package kdquiz.users.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SignInDto {

    private String email;
    private String password;
}
