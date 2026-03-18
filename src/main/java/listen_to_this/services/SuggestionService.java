package listen_to_this.services;

import listen_to_this.repositories.SuggestionRepo;
import org.springframework.beans.factory.annotation.Autowired;

public class SuggestionService {

    @Autowired
    private SuggestionRepo suggestionRepo;
}
