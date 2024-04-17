package kdquiz.users.repository;

import kdquiz.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String userEmail);
    Optional<Users> findByNickname(String nickname);

//    Users findByEmail(String userEmail);
}
