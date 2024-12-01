package edutech.backend.service;

import edutech.backend.entity.User;
import edutech.backend.exception.CustomException;
import edutech.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Method to hash the password
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    // Method to check if the password is correct
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    // Method to find a user by email
    public User findByEmail(String email) throws CustomException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new CustomException("User not found with email: " + email);
        }
    }

    // Method to save a user
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
