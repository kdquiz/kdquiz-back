package kdquiz.quiz.repository;

import kdquiz.domain.Choice;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface ChoiceRepository extends JpaRepository<Choice, Long> {

    Optional<Choice> findById(Long id);
    List<Choice> findByQuestion_Id(Long id);
}
