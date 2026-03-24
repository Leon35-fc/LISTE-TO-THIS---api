package listen_to_this.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "suggestions")
public class Suggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Long songId;

    @Column(nullable = false)
    private Long suggestionId;

    @Column(nullable = false)
    private int vote;

    public Suggestion() {
    }

    public Suggestion(Long songId, Long suggestionId, int vote) {
        this.songId = songId;
        this.suggestionId = suggestionId;
        this.vote = 0;
    }

    public Long getSongId() {
        return songId;
    }

    public Long getSuggestionId() {
        return suggestionId;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    @Override
    public String toString() {
        return "Suggestion{" +
                "songId=" + songId +
                ", suggestionId=" + suggestionId +
                ", vote=" + vote +
                '}';
    }
}
