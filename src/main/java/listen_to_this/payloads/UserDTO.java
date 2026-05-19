package listen_to_this.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserDTO(
        @NotBlank(message = "Username is a required field.")
        @Size(min = 4, max = 30, message = "Username have to be between 4 and 30 characters long.")
        String username,
        @NotBlank(message = "E-mail is a required field.")
        @Email(message = "E-mail is in the wrong format.")
        String email,
        @NotBlank(message = "Password is a required field")
        @Size(min = 4, message = "Password has to be at least 4 characters long.")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{4,}$", message = "Password have to contain at least 1 Uppercase character, 1 Lowercase character, 1 number ")
        String password

) {
}
