package kdquiz.participants.repository;


import kdquiz.domain.Participants;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipantsRepositroy extends JpaRepository<Participants, Long> {
    Optional<Participants> deleteAllByQuizId(long quizId);
}
