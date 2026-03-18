package listen_to_this.services;

import listen_to_this.entities.User;
import listen_to_this.exceptions.BadRequestException;
import listen_to_this.exceptions.NotFoundException;
import listen_to_this.payloads.UserDTO;
import listen_to_this.repositories.UserRepo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserService.class);
    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User getById(UUID id) {
        return this.userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + " not found."));

    }


    public User save(UserDTO payload) {
        this.userRepo.findByEmail(payload.email()).ifPresent(user -> {
            throw new BadRequestException("E-mail " + user.getEmail() + " already in use.");
        });

        User newUser = new User(payload.username(), payload.email(), payload.password());
        newUser.setProfileImage("https://ui-avatars.com/api?name=" + payload.username());

        User savedUser = this.userRepo.save(newUser);
        log.info("User with id " + savedUser.getId() + " correctly saved!");
        return savedUser;
    }

    public User findByEmail(String email) {
        return this.userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with e-mail " + email + " not found."));
    }


}
