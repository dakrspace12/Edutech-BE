package edutech.backend.util;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    // Method to encode a plain text password
    public String encodePassword(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return PASSWORD_ENCODER.encode(rawPassword);
    }

    // Method to validate if a raw password matches the encoded password
    public boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            throw new IllegalArgumentException("Raw password and encoded password cannot be null");
        }
        return PASSWORD_ENCODER.matches(rawPassword, encodedPassword);
    }

    // Method to check password strength
    public boolean isPasswordStrong(String password) {
        if (password == null || password.isEmpty()) {
            return false; // or throw an exception depending on your use case
        }
        // Enhanced password strength check: min 8 chars, 1 upper, 1 lower, 1 number, 1 special char
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&  // at least one uppercase letter
                password.matches(".*[a-z].*") &&  // at least one lowercase letter
                password.matches(".*[0-9].*") &&  // at least one digit
                password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");  // at least one special character
    }
}
