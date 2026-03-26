package listen_to_this.payloads;

public record SuggestionResponseDTO(
        long suggestionId,
        int vote,
        String title,
        String artist,
        String albumTitle,
        String preview
) {
}
