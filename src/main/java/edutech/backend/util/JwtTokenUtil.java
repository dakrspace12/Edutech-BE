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

    public JwtTokenUtil() {
        try {
            System.out.println("Initializing JwtTokenUtil with automatically generated key.");
            this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        } catch (Exception e) {
            System.err.println("Error initializing JwtTokenUtil: " + e.getMessage());
            throw e;
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username, Set<Role> roles) {
        String[] roleNames = roles.stream()
                .map(role -> role.getName().name())
                .toArray(String[]::new);

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roleNames)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Set<Role> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        String[] roles = claims.get("roles", String[].class);

        return Arrays.stream(roles)
                .map(roleName -> new Role(Role.RoleName.valueOf(roleName)))
                .collect(Collectors.toSet());
    }

    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}
