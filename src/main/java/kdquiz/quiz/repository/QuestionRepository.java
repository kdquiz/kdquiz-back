package kdquiz.quiz.repository;

import kdquiz.quiz.domain.Questions;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Questions, Long> {

    Optional<Questions> findById(Long id);
}
