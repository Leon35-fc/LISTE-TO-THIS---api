package listen_to_this.payloads;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SuggestionResponseDTO(
        long id,
        int vote,
        String title,
        Artist artist,
        String albumTitle,
        String preview,
        Album album
) {
    public record Artist(String name) {
    }

    public record Album(
            @JsonProperty("cover_xl") String coverXl
    ) {
    }
}
