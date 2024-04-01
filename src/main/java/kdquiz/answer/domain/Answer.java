package kdquiz.quiz.domain;

import jakarta.persistence.*;
import kdquiz.participants.domain.Participants;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Setter
@Getter
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "participant_id", nullable = false)
    private Participants participants;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Questions questions;

    @ManyToOne
    @JoinColumn(name = "choice_id", nullable = false)
    private Choice choice;

    @Column
    private String content;

    @Column
    private boolean isCorrect;

    @Column(columnDefinition = "int default 0")
    private int score;

}
