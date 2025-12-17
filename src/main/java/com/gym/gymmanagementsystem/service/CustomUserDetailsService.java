package com.gym.management.service;

import com.gym.management.entity.User;
import com.gym.management.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. අපේ Database එකෙන් User ව හොයනවා
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 2. Spring Security එකට තේරෙන භාෂාවට (UserDetails) අපේ User ව හරවනවා
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // මෙතන තියෙන්නේ Encrypt වුන password එක
                .roles(user.getRole().replace("ROLE_", "")) // "ROLE_MEMBER" -> "MEMBER" විදියට දෙනවා
                .build();
    }
}