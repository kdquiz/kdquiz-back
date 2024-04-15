package kdquiz.users.repository;

import kdquiz.users.domain.EmailCheck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailCheckRepository extends JpaRepository<EmailCheck, String> {
    void deleteByCreatedAtBefore(LocalDateTime dateTime);

    Optional<EmailCheck> findByEmailAndAuth(String Email, String Auth);

    Optional<EmailCheck> deleteAllByEmailAndAuth(String Email, String Auth);
}
