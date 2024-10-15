package kdquiz.quiz.repository;

import kdquiz.domain.Quiz;
import kdquiz.domain.QuizImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizImgRepository extends JpaRepository<QuizImg, String> {
    List<QuizImg> findByQuestionId(Long questionId);
    Optional<QuizImg> findByFileName(String fileName);
}
