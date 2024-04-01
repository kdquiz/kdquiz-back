package kdquiz.quiz.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.aspectj.weaver.patterns.TypePatternQuestions;

@Entity
@Getter
@Setter
@ToString
public class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Questions question;

    @Column
    private String content;

    @Column
    private Boolean isCorrect;
}
