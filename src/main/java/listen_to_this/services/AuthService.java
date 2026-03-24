package listen_to_this.services;

import listen_to_this.entities.User;
import listen_to_this.exceptions.UnauthorizedException;
import listen_to_this.payloads.LoginDTO;
import listen_to_this.payloads.LoginResponseDTO;
import listen_to_this.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;
    private final JWTTools jwtTools;
    private final PasswordEncoder bcrypt;

    @Autowired
    public AuthService(UserService userService, JWTTools jwtTools, PasswordEncoder bcrypt) {
        this.userService = userService;
        this.jwtTools = jwtTools;
        this.bcrypt = bcrypt;
    }

    public LoginResponseDTO checkCredentialAndGenerateToken(LoginDTO body) {
        User found = this.userService.findByEmail(body.email());

        if (bcrypt.matches(body.password(), found.getPassword())) {
            String accessToken = jwtTools.generateToken(found);
            return new LoginResponseDTO(accessToken);
        } else {
            throw new UnauthorizedException("Credentials are wrong!");
        }
    }
}