package edutech.backend.entity;



import jakarta.persistence.*;
import lombok.Data;

import java.time.Duration;
import java.time.Instant;

@Data
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    @Column(nullable = false)
    private boolean revoked; // Track if the token is revoked

    @PrePersist
    public void prePersist() {
        if (expiryDate == null) {
            expiryDate = Instant.now().plus(Duration.ofDays(7)); // Default expiry time for refresh token
        }
        revoked = false; // Token is not revoked initially
    }
}
