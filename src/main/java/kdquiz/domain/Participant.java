package kdquiz.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long quizId;

    @Column(nullable = false)
    private String nickname;

    @Column
    private int score=0;

    @Column
    private Integer ranking=0;

}
