package com.gym.management.repository;

import com.gym.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; // <--- මේ පේළිය තමයි ඔයාට මගහැරිලා තිබුනේ

public interface UserRepository extends JpaRepository<User, Long> {

    // Username එකෙන් User කෙනෙක් හොයන්න (Login එකට)
    java.util.Optional<User> findByUsername(String username);

    // Search Bar එකට (නමේ කොටසක් ගැහුවත් එන්න)
    List<User> findByUsernameContainingIgnoreCase(String keyword);
}