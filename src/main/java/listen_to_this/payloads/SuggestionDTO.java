package listen_to_this.payloads;

public record SuggestionDTO(
        Long songId,
        Long suggestionId,
        int vote
) {
}
