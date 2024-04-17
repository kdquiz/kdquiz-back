package kdquiz.quiz.repository;

import kdquiz.domain.Questions;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Questions, Long> {

    Optional<Questions> findById(Long id);

    List<Questions> findByQuiz_Id(Long id);
}
