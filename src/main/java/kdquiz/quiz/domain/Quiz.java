package kdquiz.quiz.domain;

import jakarta.persistence.*;
import kdquiz.users.domain.Users;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@ToString
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String type;

    @Column
    private String pin;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.REMOVE)
    private List<Questions> questions;

}
