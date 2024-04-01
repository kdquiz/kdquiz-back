package kdquiz.users.repository;

import kdquiz.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsersRepository extends JpaRepository<Users, Long> {
}
