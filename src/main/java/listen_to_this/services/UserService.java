package listen_to_this.services;

import listen_to_this.entities.User;
import listen_to_this.exceptions.BadRequestException;
import listen_to_this.exceptions.NotFoundException;
import listen_to_this.payloads.UserDTO;
import listen_to_this.repositories.UserRepo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserService.class);
    private final UserRepo userRepo;
    private final PasswordEncoder bcrypt;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder bcrypt) {
        this.userRepo = userRepo;
        this.bcrypt = bcrypt;
    }

    public User findById(UUID id) {
        return this.userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + " not found."));

    }

    public User findByEmail(String email) {
        return this.userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with e-mail " + email + " not found."));
    }

    public User findByUsername(String username) {
        return this.userRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User with username " + username + " not found."));
    }

    public User save(UserDTO payload) {
        this.userRepo.findByEmail(payload.email()).ifPresent(user -> {
            throw new BadRequestException("E-mail " + user.getEmail() + " already in use.");
        });

        this.userRepo.findByUsername(payload.username()).ifPresent(user -> {
            throw new BadRequestException("Username " + user.getUsername() + " already in use.");
        });

        User newUser = new User(payload.username(), payload.email(), bcrypt.encode(payload.password()));
        newUser.setProfileImage("https://ui-avatars.com/api?name=" + payload.username());

        User savedUser = this.userRepo.save(newUser);
        log.info("User with id " + savedUser.getId() + " correctly saved!");
        return savedUser;
    }

    public Page<User> findAll(int page, int size, String orderBy, String sortCriteria) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest
                .of(page, size, sortCriteria.equals("desc") ? Sort.by(orderBy).descending() : Sort.by(orderBy));
        return this.userRepo.findAll(pageable);
    }

    public User findByIdAndUpdate(UUID id, UserDTO payload) {
        User found = this.findById(id);

        if (!found.getEmail().equals(payload.email())) {
        }

        User updatedUser = new User();
        return updatedUser;
    }
    
}
