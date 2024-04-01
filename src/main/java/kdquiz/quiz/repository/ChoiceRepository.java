package kdquiz.quiz.repository;

import kdquiz.quiz.domain.Choice;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface ChoiceRepository extends JpaRepository<Choice, Long> {

    Optional<Choice> findById(Long id);
}
