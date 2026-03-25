package listen_to_this.controllers;

import listen_to_this.entities.User;
import listen_to_this.payloads.UserResponseDTO;
import listen_to_this.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Page<User> findAll(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "surname") String orderBy,
                              @RequestParam(defaultValue = "asc") String sortCriteria) {

        return this.userService.findAll(page, size, orderBy, sortCriteria);
    }

    @GetMapping("/me")
    public UserResponseDTO getProfile(@AuthenticationPrincipal User currentUser) {
        return new UserResponseDTO(
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentUser.getProfileImage()
        );
    }
    
//    @PutMapping("/me")
//    public User findByIdAndUpdate(@PathVariable UUID userId, @RequestBody UserDTO payload) {
//        return this.userService.findByIdAndUpdate()
//    }

}