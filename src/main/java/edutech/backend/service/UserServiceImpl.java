package edutech.backend.service;

import edutech.backend.dto.*;
import edutech.backend.entity.User;
import edutech.backend.exception.CustomException;
import edutech.backend.repository.UserRepository;
import edutech.backend.util.JwtTokenUtil;
import edutech.backend.util.MessageConstant;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;


    private final JwtTokenUtil jwtUtil;

    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenUtil jwtUtil, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    @Override
    public ApiResponse<Void> registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new CustomException("Email is already in Use");
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));  // Encrypt the password
        user.setEmail(signupRequest.getEmail());

        // Save the user in the repository
        userRepository.save(user);
        return new ApiResponse<>(true, "User registered successfully", null);
    }

    @Override
    public String authenticateUser(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new CustomException("Invalid username or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new CustomException("Invalid username or password");
        }
        logger.info("User authenticated successfully with username: {}", loginRequest.getUsername());
        return jwtUtil.generateToken(user.getUsername(), user.getRoles());
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found with ID: " + id));
        return convertToDto(user);
    }

    @Override
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new CustomException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
        logger.info("User deleted successfully with ID: {}", id);
    }

    // New methods for password reset
    @Override
    public void requestPasswordReset(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String token = jwtUtil.generateToken(user.getUsername(), user.getRoles());
            String resetLink = "http://localhost:8080/api/v1/users/reset-password?token=" + token;
            emailService.sendSimpleMessage(email, "Password Reset Request", "Click the link to reset your password: " + resetLink);
            logger.info("Password reset link sent to {}", email);
        } else {
            throw new CustomException("Email address not found");
        }
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        Claims claims = jwtUtil.extractResetPasswordClaims(token);
        if (Objects.isNull(claims)) {
            throw new CustomException("Invalid token");
        }

        String username = claims.getSubject();
        @SuppressWarnings("unchecked")
        List<String> roles = claims.get("roles", List.class);

        // Check if user has the required role (e.g., ROLE_ADMIN or ROLE_USER)
        if (!roles.contains("ROLE_ADMIN") && !roles.contains("ROLE_USER")) {
            throw new CustomException("User does not have the required role");
        }

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            logger.info("Password reset successfully for user: {}", username);
        } else {
            throw new CustomException(MessageConstant.INVALID_TOKEN_OR_USER_NOT_FOUND);
        }
    }


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