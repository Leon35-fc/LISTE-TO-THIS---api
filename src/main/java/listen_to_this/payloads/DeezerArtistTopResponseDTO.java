package listen_to_this.payloads;

import java.util.List;

public record DeezerArtistTopResponseDTO(
        List<SuggestionResponseDTO> data
) {
}
