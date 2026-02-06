package com.gym.management.service;

import com.gym.management.entity.User;
import com.gym.management.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 1. User save
    public void saveUser(User user) {
        // Password Encrypt
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Role auto assign "MEMBER"
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("MEMBER");
        }

        userRepository.save(user);
    }

    // 2. User List )
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 3. User findby ID
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // 4. User delete
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}