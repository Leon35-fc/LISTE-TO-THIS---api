package listen_to_this.payloads;

import java.util.Set;

public record UserResponseDTO(String username, String email, String profileImage, Set<Long> favourites) {
}
