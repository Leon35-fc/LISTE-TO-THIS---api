package listen_to_this.services;

import listen_to_this.entities.Suggestion;
import listen_to_this.payloads.DeezerArtistTopResponseDTO;
import listen_to_this.payloads.DeezerTrackDTO;
import listen_to_this.payloads.SuggestionDTO;
import listen_to_this.payloads.SuggestionResponseDTO;
import listen_to_this.repositories.SuggestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
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

    public static SuggestionResponseDTO fromDeezer(DeezerTrackDTO track) {
        String artistName = "Unknown Artist";
        if (track.artist() != null) {
            artistName = track.artist().name();
        }

        String albumTitle = "Unknown Album";
        if (track.album() != null) {
            albumTitle = track.album().albumTitle();
        }

        return new SuggestionResponseDTO(
                track.id(),
                0,
                track.title(),
                new SuggestionResponseDTO.Artist(artistName),
                albumTitle,
                track.preview(),
                new SuggestionResponseDTO.Album(track.album().coverXL())
        );
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

    public DeezerTrackDTO deezerFetch(String uri) {
        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(DeezerTrackDTO.class);
    }

    public DeezerTrackDTO deezerFetch(String uri, long variableURI) {
        return restClient.get()
                .uri(uri, variableURI)
                .retrieve()
                .body(DeezerTrackDTO.class);
    }

    public long getArtistIdBySongId(long songId) {
//        DeezerTrackDTO songData = restClient.get()
//                .uri("https://striveschool-api.herokuapp.com/api/deezer/artist/{songId}", songId)
//                .retrieve()
//                .body(DeezerTrackDTO.class);
        DeezerTrackDTO songData = deezerFetch("https://striveschool-api.herokuapp.com/api/deezer/artist/{songId}", songId);
        assert songData != null;
        return songData.artist().id();
    }

    @Cacheable(value = "suggestions", key = "#a0")
    public List<SuggestionResponseDTO> getSuggestedData(Long songId) {
        List<Suggestion> suggestions = suggestionRepo.findAllBySongId(songId);
        if (suggestions.isEmpty()) {

            DeezerTrackDTO artistResponse = deezerFetch("/track/{id}", songId);

//            System.out.println("Se non trovo il suggerimento... " + URI.create(artistResponse.artist().tracklist()).getPath() + "?limit=50");

            DeezerArtistTopResponseDTO artistTopResponse = restClient.get()
                    .uri(URI.create(artistResponse.artist().tracklist()).getPath() + "?limit=50")
                    .retrieve()
                    .body(DeezerArtistTopResponseDTO.class);

            List<SuggestionResponseDTO> suggestRespDTO = artistTopResponse.data()
                    .stream().map(track -> {
                        return new SuggestionResponseDTO(
                                track.id(),
                                0,
                                track.title(),
                                new SuggestionResponseDTO.Artist(track.artist().name()),
                                track.album().albumTitle(),
                                track.preview(),
                                new SuggestionResponseDTO.Album(track.album().coverXL())

                        );
                    }).toList();
            System.out.println("--- Chiamata al database per ID: " + songId);
            return suggestRespDTO;
        } else {
            return suggestions.stream()
                    .map(s -> {
//                        System.out.println("/track/" + s.getSongId());
                        DeezerTrackDTO track = restClient.get()
                                .uri("/track/{id}", s.getSongId())
                                .retrieve()
                                .body(DeezerTrackDTO.class);
//                        System.out.println(track.artist().tracklist());
                        return new SuggestionResponseDTO(s.getSuggestionId(), s.getVote(), track.title(),
                                new SuggestionResponseDTO.Artist(track.artist().name()),
                                track.album().albumTitle(),
                                track.preview(),
                                new SuggestionResponseDTO.Album(track.album().coverXL())

                        );
                    })
                    .toList();
        }
    }
}
