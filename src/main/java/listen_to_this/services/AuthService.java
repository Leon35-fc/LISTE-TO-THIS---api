package listen_to_this.services;

import listen_to_this.entities.User;
import listen_to_this.exceptions.UnauthorizedException;
import listen_to_this.payloads.LoginDTO;
import listen_to_this.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;
    private final JWTTools jwtTools;

    @Autowired
    public AuthService(UserService userService, JWTTools jwtTools) {
        this.userService = userService;
        this.jwtTools = jwtTools;
    }

    public String checkCredentialAndGenerateToken(LoginDTO body) {
        User found = this.userService.findByEmail(body.email());

        if (found.getPassword().equals(body.password())) {
            String accessToken = jwtTools.generateToken(found);
            return accessToken;
        } else {
            throw new UnauthorizedException("Credentials are wrong!");
        }
    }
}
