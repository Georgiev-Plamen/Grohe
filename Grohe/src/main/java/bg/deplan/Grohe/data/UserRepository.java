package bg.deplan.Grohe.data;

import bg.deplan.Grohe.model.User;
import bg.deplan.Grohe.service.AppUserDetailsService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
