package listen_to_this.repositories;

import listen_to_this.entities.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SuggestionRepo extends JpaRepository<Suggestion, UUID> {

    public Suggestion findBySongId(Long songId);
}
