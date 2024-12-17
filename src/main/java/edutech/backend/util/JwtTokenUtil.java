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
public class JwtTokenUtil {

    private final SecretKey secretKey;

    @Value("${jwt.expirationMs}")
    private Long expirationMs;

    // Constructor to initialize the secret key using a secure method
    public JwtTokenUtil() {
        try {
            // For production, consider loading from a secure location like environment variables or config files
            System.out.println("Initializing JwtTokenUtil with automatically generated key.");
            this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);  // Guarantees a 256-bit key
        } catch (Exception e) {
            System.err.println("Error initializing JwtTokenUtil: " + e.getMessage());
            throw e;
        }
    }

    // Extract the username from the JWT token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract expiration date from the JWT token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract any claim from the JWT token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Parse and extract all claims from the JWT token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if the token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Generate the JWT token with username and roles (array form)
    public String generateToken(String username, Set<Role> roles) {
        // Convert roles to an array of strings
        String[] roleNames = roles.stream()
                .map(role -> role.getName().name())
                .toArray(String[]::new);

        // Create the JWT token with roles as a claim
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roleNames)  // Store roles as an array
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
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
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
    // Extract claims specifically for reset password tokens
    public Claims extractResetPasswordClaims(String token) {
        try {
            return extractAllClaims(token);
        } catch (Exception e) {
            System.err.println("Failed to extract claims from reset password token: " + e.getMessage());
            return null;
        }
    }
}