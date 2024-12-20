package edutech.backend.util;


import edutech.backend.entity.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {
    private static final Logger logger = Logger.getLogger(JwtTokenUtil.class.getName());
    private final SecretKey secretKey;

    @Value("${jwt.expirationMs}")
    private Long expirationMs;


    public JwtTokenUtil() {
        try {

            logger.info("Initializing JwtTokenUtil with automatically generated key.");
            this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        } catch (Exception e) {
            logger.severe("Error initializing JwtTokenUtil: " + e.getMessage());
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


    public Set<Role> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        String[] roles = claims.get("roles", String[].class);

        // Convert the array of strings into Role objects
        return Arrays.stream(roles)
                .map(roleName -> new Role(Role.RoleName.valueOf(roleName)))
                .collect(Collectors.toSet());
    }


    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
    // Extract claims specifically for reset password tokens
    public Claims extractResetPasswordClaims(String token) {
        try {
            return extractAllClaims(token);
        } catch (Exception e) {
            logger.severe("Failed to extract claims from reset password token: " + e.getMessage());
            return null;
        }
    }
}