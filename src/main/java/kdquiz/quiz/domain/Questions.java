package kdquiz.quiz.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
@ToString
public class Questions {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(nullable = false, updatable = false)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "quiz_id", nullable = false)
        private Quiz quiz;

        @Column
        private String content;

        @Column
        private int score;

        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "created_at")
        private LocalDateTime createdAt;

        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "deleted_at")
        private LocalDateTime deletedAt;

        @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
        private List<Choice> Choice;

}
