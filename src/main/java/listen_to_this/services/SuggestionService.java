package listen_to_this.services;

import listen_to_this.entities.Suggestion;
import listen_to_this.payloads.DeezerArtistTopResponseDTO;
import listen_to_this.payloads.DeezerTrackDTO;
import listen_to_this.payloads.SuggestionDTO;
import listen_to_this.payloads.SuggestionResponseDTO;
import listen_to_this.repositories.SuggestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SuggestionService {

    private final RestClient restClient;
    @Autowired
    private SuggestionRepo suggestionRepo;

    public SuggestionService(RestClient restClient, SuggestionRepo suggestionRepo) {
        this.restClient = restClient;
        this.suggestionRepo = suggestionRepo;
    }

    public Optional<Suggestion> findBySongId(Long songId) {
        return this.suggestionRepo.findBySongId(songId);
    }

    public Suggestion save(SuggestionDTO payload) {

        Optional<Suggestion> found = this.suggestionRepo.findBySongIdAndSuggestionId(payload.songId(), payload.suggestionId());
        if (found.isPresent()) {
            found.get().setVote(found.get().getVote() + 1);
            return suggestionRepo.save(found.get());
        }

        Suggestion newSuggestion = new Suggestion(payload.songId(), payload.suggestionId(), payload.vote());

        return this.suggestionRepo.save(newSuggestion);
    }

    public long getArtistIdBySongId(long songId) {
        DeezerTrackDTO songData = restClient.get()
                .uri("https://striveschool-api.herokuapp.com/api/deezer/artist/{songId}", songId)
                .retrieve()
                .body(DeezerTrackDTO.class);

        assert songData != null;
        return songData.artist().id();
    }

    public List<SuggestionResponseDTO> getSuggestedData(Long songId) {
        List<Suggestion> suggestions = suggestionRepo.findAllBySongId(songId);
        if (suggestions.isEmpty()) {
            long artistId = getArtistIdBySongId(songId);

            DeezerArtistTopResponseDTO artistResponse = restClient.get()
                    .uri("/artist/{artistId}/top?limit=50", artistId)
                    .retrieve()
                    .body(DeezerArtistTopResponseDTO.class);

            return artistResponse != null ? artistResponse.data() : Collections.emptyList();
        } else {
            return suggestions.parallelStream()
                    .map(id -> restClient.get()
                            .uri("/track/{id}", id.getSongId())
                            .retrieve()
                            .body(SuggestionResponseDTO.class))
                    .toList();
        }
    }
}
