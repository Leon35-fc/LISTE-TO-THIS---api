package listen_to_this.webclients;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientConfig {

    @Bean
    public RestClient suggestionRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl("https://striveschool-api.herokuapp.com/api/deezer")
                .defaultHeader("Accept", "application/json")
                .build();
    }
}