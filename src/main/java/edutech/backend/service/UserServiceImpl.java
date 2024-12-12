package edutech.backend.service;

import edutech.backend.dto.ApiResponse;
import edutech.backend.dto.LoginRequest;
import edutech.backend.dto.SignupRequest;
import edutech.backend.dto.UserDto;
import edutech.backend.entity.User;
import edutech.backend.exception.CustomException;
import edutech.backend.repository.UserRepository;
import edutech.backend.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtUtil;

    // Register a new user
    public ApiResponse<Void> registerUser(SignupRequest signupRequest) {
        // Check if the email is already in use
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new CustomException("Email is already in Use");
        }

        // Create a new user object
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));  // Encrypt the password
        user.setEmail(signupRequest.getEmail());

        // Save the user in the repository
        userRepository.save(user);
        return new ApiResponse<>(true, "User registered successfully", null);
    }

    // Authenticate user
    public String authenticateUser(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new CustomException("Invalid username or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new CustomException("Invalid username or password");
        }

        return jwtUtil.generateToken(user.getUsername(), user.getRoles());
    }

    // Get all users
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Get user by ID
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found with ID: " + id));
        return convertToDto(user);
    }

    // Delete user by ID
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new CustomException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    // Helper method to convert User to UserDto
    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet()));
        return userDto;
    }
}
