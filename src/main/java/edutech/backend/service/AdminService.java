package edutech.backend.service;

import edutech.backend.entity.User;

import java.util.List;

public interface AdminService {
    List<User> getAllUsers();
    User getUserById(Long id);
    void deleteUserById(Long id);
}
