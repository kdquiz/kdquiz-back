package kdquiz.users.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
public class EmailCheck {
    @Id
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String auth;

    @Column(nullable = false)
    private LocalDateTime createdAt;

}
