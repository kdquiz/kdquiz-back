package kdquiz.game.repository;


import kdquiz.domain.Participants;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantsRepositroy extends JpaRepository<Participants, Long> {
    Optional<Participants> deleteAllByQuizId(long quizId);
    Participants findByNicknameAndQuizId(String nickname, Long QuizId);
    Optional<Participants> findById(Long id);
    List<Participants> findByQuizIdOrderByScoreDesc(Long quizId);
    List<Participants> findByQuizId(long QuizId);

}
