package edutech.backend.service;

import edutech.backend.dto.LoginRequest;
import edutech.backend.dto.SignupRequest;
import edutech.backend.entity.Role;
import edutech.backend.entity.User;
import edutech.backend.repository.RoleRepository;
import edutech.backend.repository.UserRepository;
import edutech.backend.security.CustomUserDetails;
import edutech.backend.util.JwtTokenUtil;
import edutech.backend.util.MessageConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtTokenUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public String registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername()) || userRepository.existsByEmail(signUpRequest.getEmail())) {
            return MessageConstant.USERNAME_OR_EMAIL_IS_ALREADY_IN_USE;
        }

        if (signUpRequest.getRole() == null || signUpRequest.getRole().isEmpty()) {
            signUpRequest.setRole(Set.of("ROLE_USER"));
        }

        Set<Role> roles = new HashSet<>();
        for (String roleName : signUpRequest.getRole()) {
            try {
                Role.RoleName roleEnum = Role.RoleName.valueOf(roleName);
                Role role = roleRepository.findByName(roleEnum)
                        .orElseThrow(() -> new RuntimeException(MessageConstant.ROLE_NOT_FOUND + roleEnum));
                roles.add(role);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(MessageConstant.INVALID_ROLE_NAME_PROVIDED + roleName, e);
            }
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRoles(roles);
        userRepository.save(user);

        return jwtUtil.generateToken(user.getUsername(), user.getRoles());
    }

    @Override
    public String authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return jwtUtil.generateToken(authentication.getName(), userDetails.getRoles());
    }
}
