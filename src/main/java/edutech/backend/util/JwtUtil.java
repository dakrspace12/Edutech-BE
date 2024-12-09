package edutech.backend.util;

import edutech.backend.entity.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

    @Value("${jwt.expirationMs}")
    private Long expirationMs; // Set the expiration time from application.properties

    // Constructor to initialize the secret key
    public JwtUtil() {
        try {
            // Use the secretKeyFor method to generate a secure key for HS256
            this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            System.out.println("Initialized JwtUtil with secure key.");
        } catch (Exception e) {
            System.err.println("Error initializing JwtUtil: " + e.getMessage());
            throw e; // Ensure this stops execution if the key is invalid
        }
    }

    // Extract the username from the JWT token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // Get the subject (username) of the token
    }

    // Extract expiration date from the JWT token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration); // Extract expiration date from the token
    }

    // Extract any claim from the JWT token using a function (like extracting the username or expiration date)
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token); // Get all claims from the token
        return claimsResolver.apply(claims); // Apply the given function to extract specific claim
    }

    // Parse and extract all claims from the JWT token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey) // Set the signing key
                .build()
                .parseClaimsJws(token) // Parse the token
                .getBody(); // Return the body of the claims
    }

    // Check if the token is expired by comparing the expiration date with the current time
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // Return true if expired
    }

    // Generate the JWT token with username and roles
    public String generateToken(String username, Set<Role> roles) {
        // Convert roles to an array of role names (instead of a comma-separated string)
        String[] roleNames = roles.stream()
                .map(role -> role.getName().name()) // Assuming Role has an enum 'name' for role names
                .toArray(String[]::new);  // Collect roles into an array

        // Create the JWT token with the subject (username), roles as a claim, issued and expiration time
        return Jwts.builder()
                .setSubject(username)  // Subject is the username
                .claim("roles", roleNames)  // Add roles as a claim
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Set the issued date
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))  // Set the expiration
                .signWith(secretKey, SignatureAlgorithm.HS256)  // Sign the JWT with the secret key
                .compact();  // Build the compact JWT token
    }

    // Extract roles from the token
    public Set<Role> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        String[] roles = claims.get("roles", String[].class);  // Extract roles as an array of strings

        // Convert the array of strings into Role objects
        return Arrays.stream(roles)
                .map(roleName -> new Role(Role.RoleName.valueOf(roleName)))  // Map each string to Role
                .collect(Collectors.toSet());
    }

    // Validate the JWT token by checking username and expiration
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token); // Extract username from the token
        return (extractedUsername.equals(username) && !isTokenExpired(token)); // Return true if the token is valid
    }
}
