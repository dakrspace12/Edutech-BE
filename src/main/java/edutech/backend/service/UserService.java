package edutech.backend.service;

import edutech.backend.dto.LoginRequest;
import edutech.backend.dto.SignupRequest;
import edutech.backend.entity.User;
import edutech.backend.repository.UserRepository;
import edutech.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Register a new user
    public String registerUser(SignupRequest signupRequest) {
        // Check if the email is already in use
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return "Email is already in use";  // Handle email duplication
        }

        // Create a new user object
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));  // Encrypt the password
        user.setEmail(signupRequest.getEmail());

        // Save the user in the repository
        userRepository.save(user);
        return "User registered successfully";
    }

    // Authenticate user
    public String authenticateUser(LoginRequest loginRequest) {
        // Find the user by username (make sure to use Optional here)
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Check if the password matches the one stored in the database
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                // Generate JWT token with the user's roles
                return jwtUtil.generateToken(user.getUsername(), user.getRoles());
            }
        }
        return null;  // Return null if the user is not found or password doesn't match
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();  // Fetch all users from the repository
    }

    // Get user by ID
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);  // Return the user if found, otherwise null
    }

    // Delete user by ID
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);  // Delete the user by ID
    }
}
