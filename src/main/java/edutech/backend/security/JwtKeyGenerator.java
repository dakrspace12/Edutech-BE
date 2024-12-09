package edutech.backend.security;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Base64;

public class JwtKeyGenerator {
    public static void main(String[] args) {
        // Generate the secret key for HMAC-SHA256 (HS256)
        SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

        // Convert the key to a Base64-encoded string
        String base64EncodedKey = Base64.getEncoder().encodeToString(key.getEncoded());

        // Print the Base64-encoded key
        System.out.println("Base64-encoded JWT Secret Key: " + base64EncodedKey);
    }
}
