package kdquiz.game.repository;


import kdquiz.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantsRepositroy extends JpaRepository<Participant, Long> {
    Optional<Participant> deleteAllByQuizId(long quizId);
    Participant findByNicknameAndQuizId(String nickname, Long QuizId);
    Optional<Participant> findById(Long id);
    List<Participant> findByQuizIdOrderByScoreDesc(Long quizId);
    List<Participant> findByQuizId(long QuizId);
    Optional<Participant> findByQuizIdAndId(long QuizId, Long Id);

}
