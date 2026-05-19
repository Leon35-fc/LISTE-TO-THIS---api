package listen_to_this.repositories;

import listen_to_this.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {

//    Optional<User> getById(UUID id);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}
