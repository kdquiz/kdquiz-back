package kdquiz.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDto {
    @Email
    @NotEmpty(message = "이메일을 입력해주새요")
    private String email;
}
