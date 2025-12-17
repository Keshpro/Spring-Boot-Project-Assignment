package com.gym.management.service;

import com.gym.management.entity.User;
import com.gym.management.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List; // List එක import කරන්න ඕන

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // User කෙනෙක් Save කරන method එක
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null) {
            user.setRole("ROLE_MEMBER");
        }

        userRepository.save(user);
    } // <--- 1. saveUser මෙතඩ් එක මෙතනින් ඉවර වෙන්න ඕන

    // --- අලුත් කොටස් පටන් ගන්නේ මෙතනින් (saveUser එකට එලියෙන්) ---

    // 2. ඔක්කොම Users ලා ගන්න
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 3. ID එකෙන් User කෙනෙක් හොයාගන්න
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}