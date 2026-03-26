package listen_to_this.controllers;

import listen_to_this.entities.Suggestion;
import listen_to_this.payloads.SuggestionDTO;
import listen_to_this.services.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/suggestions")
public class SuggestionController {

    @Autowired
    private SuggestionService suggestionService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Suggestion addOrUpdateSuggestion(@RequestBody SuggestionDTO suggestion) {

        return this.suggestionService.save(suggestion);
    }
}
