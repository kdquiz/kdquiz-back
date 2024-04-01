package kdquiz.quiz.repository;

import kdquiz.quiz.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AnswerRepository extends JpaRepository<Answer, Long> {
    boolean existsByScoreNot(int score);
}
