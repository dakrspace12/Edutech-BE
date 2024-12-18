package edutech.backend.service;

import edutech.backend.dto.ApiResponse;
import edutech.backend.dto.LoginRequest;
import edutech.backend.dto.SignupRequest;
import edutech.backend.dto.UserDto;
import edutech.backend.entity.User;
import edutech.backend.exception.CustomException;
import edutech.backend.repository.UserRepository;
import edutech.backend.util.JwtTokenUtil;
import edutech.backend.util.MessageConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{


    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;


    private final JwtTokenUtil jwtUtil;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ApiResponse<Void> registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new CustomException(MessageConstant.EMAIL_IS_ALREADY_IN_USE);
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));  // Encrypt the password
        user.setEmail(signupRequest.getEmail());

        userRepository.save(user);
        return new ApiResponse<>(true, MessageConstant.USER_REGISTERED_SUCCESSFULLY, null);
    }

    @Override
    public String authenticateUser(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new CustomException(MessageConstant.INVALID_USERNAME_OR_PASSWORD));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new CustomException(MessageConstant.INVALID_USERNAME_OR_PASSWORD);
        }

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
                .orElseThrow(() -> new CustomException(MessageConstant.USER_NOT_FOUND_WITH_ID + id));
        return convertToDto(user);
    }

    @Override
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new CustomException(MessageConstant.USER_NOT_FOUND_WITH_ID + id);
        }
        userRepository.deleteById(id);
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
