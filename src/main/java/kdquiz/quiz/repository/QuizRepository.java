package kdquiz.quiz.repository;

import kdquiz.domain.Questions;
import kdquiz.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByEmail(String email); //퀴즈 조회

    Optional<Quiz> findByIdAndEmail(Long id, String email); //퀴즈 업데이트

    Optional<Quiz> findByEmailAndId(String email, Long quizId);

    Optional<Quiz> findById(Long id);

    List<Quiz> findByEmailAndTitleContaining(String email, String searchTitle);

    Quiz findByPin(int pin);

    Quiz findByPinAndEmail(int pin, String email);

    Quiz findByQuestions(List<Questions> questions);

}
