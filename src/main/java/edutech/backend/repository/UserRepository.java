package edutech.backend.repository;

import edutech.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Method to check if a user exists by username
    boolean existsByUsername(String username);

    // Method to check if a user exists by email
    boolean existsByEmail(String email);

    // You can also add other methods like finding by username or email, if needed.
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
