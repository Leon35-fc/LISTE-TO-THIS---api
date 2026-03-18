package listen_to_this.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        //Disabilita FormLogin
        httpSecurity.formLogin(formLogin -> formLogin.disable());
        //Disabilita Protezione attacchi CSRF
        httpSecurity.csrf(csrf -> csrf.disable());
        //disabilitare le sessioni
        httpSecurity.sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //Disabilita la protezione su TUTTI gli endpoint
        httpSecurity.authorizeHttpRequests(req -> req.requestMatchers("/**").permitAll());

        return httpSecurity.build();
    }
}
