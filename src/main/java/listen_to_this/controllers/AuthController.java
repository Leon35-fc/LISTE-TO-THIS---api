package listen_to_this.controllers;

import listen_to_this.entities.User;
import listen_to_this.exceptions.ValidationException;
import listen_to_this.payloads.LoginDTO;
import listen_to_this.payloads.LoginResponseDTO;
import listen_to_this.payloads.UserDTO;
import listen_to_this.services.AuthService;
import listen_to_this.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Validated LoginDTO body) {
        return this.authService.checkCredentialAndGenerateToken(body);
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Validated UserDTO payload, BindingResult validationResult) {

        if (validationResult.hasErrors()) {

            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            throw new ValidationException(errorsList);
        } else {
            return this.userService.save(payload);
        }
    }

//    @GetMapping("/lo")
//    public User getProfile(@AuthenticationPrincipal User currentAuthenticatedUser) {
//        return currentAuthenticatedUser;
//    }
}
