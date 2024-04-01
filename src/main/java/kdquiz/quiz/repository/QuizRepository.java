package kdquiz.quiz.repository;

import kdquiz.quiz.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByUsers_Id(Long id); //퀴즈 조회

    Optional<Quiz> findById(Long id); //퀴즈 업데이트



}
