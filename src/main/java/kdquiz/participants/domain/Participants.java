package kdquiz.participants.domain;

import jakarta.persistence.*;
import kdquiz.quiz.domain.Quiz;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Participants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;
    @Column(nullable = false)
    private String nickname;
}
