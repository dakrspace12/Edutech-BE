package edutech.backend.service;

import edutech.backend.dto.LoginRequest;
import edutech.backend.dto.SignupRequest;
import edutech.backend.entity.Role;
import edutech.backend.entity.User;
import edutech.backend.repository.RoleRepository;
import edutech.backend.repository.UserRepository;
import edutech.backend.security.CustomUserDetails;
import edutech.backend.util.JwtTokenUtil;
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
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;  // RoleRepository to fetch roles

    @Autowired
    private JwtTokenUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public String registerUser(SignupRequest signUpRequest) {
        // Check if the user already exists
        if (userRepository.existsByUsername(signUpRequest.getUsername()) || userRepository.existsByEmail(signUpRequest.getEmail())) {
            return "Username or Email is already in use";
        }

        // If no roles are provided, assign default 'ROLE_USER'
        if (signUpRequest.getRole() == null || signUpRequest.getRole().isEmpty()) {
            signUpRequest.setRole(Set.of("ROLE_USER"));  // Default role assignment
        }

        // Fetch roles from the database based on the request roles
        Set<Role> roles = new HashSet<>();
        for (String roleName : signUpRequest.getRole()) {
            try {
                // Convert string role to RoleName enum
                Role.RoleName roleEnum = Role.RoleName.valueOf(roleName);  // Convert String to RoleName enum
                Role role = roleRepository.findByName(roleEnum)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleEnum));
                roles.add(role);
            } catch (IllegalArgumentException e) {
                // Handle invalid role names gracefully
                throw new RuntimeException("Invalid role name provided: " + roleName, e);
            }
        }

        // Create and save the new user with roles
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));  // Ensure password is hashed
        user.setRoles(roles);
        userRepository.save(user);

        // Generate JWT token for the registered user with roles
        return jwtUtil.generateToken(user.getUsername(), user.getRoles());
    }

    public String authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Get the user details (CustomUserDetails) and pass both username and roles to generate the token
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return jwtUtil.generateToken(authentication.getName(), userDetails.getRoles());
    }
}
