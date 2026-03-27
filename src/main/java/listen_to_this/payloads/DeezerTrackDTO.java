package listen_to_this.payloads;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DeezerTrackDTO(
        long id,
        String title,
        String preview,
        Artist artist,
        Artist tracklist,
        Album album
) {
    public record Artist(long id, String name, String tracklist) {
    }

    public record Album(
            long id,
            @JsonProperty("title") String albumTitle,
            @JsonProperty("cover_xl") String coverXL
    ) {
    }
}
