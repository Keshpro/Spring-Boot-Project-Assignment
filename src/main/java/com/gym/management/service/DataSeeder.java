package com.gym.management.service;

import com.gym.management.entity.User;
import com.gym.management.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // admin කියලා කෙනෙක් Database එකේ නැත්නම් විතරක් හදන්න
        if (userRepository.findByUsername("admin").isEmpty()) {
            User user = new User();
            user.setUsername("admin");
            user.setEmail("admin@gym.com");
            user.setRole("ROLE_ADMIN"); // මෙතන තමයි Admin පවර් එක දෙන්නේ
            user.setPassword(passwordEncoder.encode("admin123")); // Password එක: admin123

            userRepository.save(user);
            System.out.println("---------------------------------------------");
            System.out.println("ADMIN USER CREATED SUCCESSFULLY");
            System.out.println("Username: admin");
            System.out.println("Password: admin123");
            System.out.println("---------------------------------------------");
        }
        // Trainer කෙනෙක් ඉන්නවද බලලා, නැත්නම් හදනවා
        if (userRepository.findByUsername("trainer").isEmpty()) {
            User trainer = new User();
            trainer.setUsername("trainer");
            trainer.setEmail("trainer@gym.com");
            trainer.setRole("ROLE_TRAINER"); // මේක තමයි Trainer Role එක
            trainer.setPassword(passwordEncoder.encode("trainer123")); // Pass: trainer123

            userRepository.save(trainer);
            System.out.println("TRAINER USER CREATED: trainer | trainer123");
        }
    }
}