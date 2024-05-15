package kdquiz.quiz.repository;

import kdquiz.domain.Options;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OptionRepository extends JpaRepository<Options, Long> {
    Optional<Options> findById(Long id);

    Optional<Options> findByQuestion_Id(long id);
}
