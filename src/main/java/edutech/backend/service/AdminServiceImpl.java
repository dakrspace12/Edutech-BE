package edutech.backend.service;

import edutech.backend.entity.User;
import edutech.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    // Get all users from the database
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();  // Retrieves all users
    }

    // Get a user by ID
    @Override
    public User getUserById(Long id) {
        // If the user doesn't exist, throw an exception
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // Delete a user by ID
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);  // Deletes the user by their ID
    }
}
