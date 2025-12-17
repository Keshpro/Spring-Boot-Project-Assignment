package com.gym.management.service;

import com.gym.management.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DataSeederImpl extends DataSeeder {
    public DataSeederImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(userRepository, passwordEncoder);
    }
}
