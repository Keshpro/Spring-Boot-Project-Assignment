package com.gym.management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users") // මේක අනිවාර්යයෙන්ම තියෙන්න ඕන
@Data // Lombok: Getters, Setters auto හැදෙනවා
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id // Primary Key එක
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID එක Auto increase වෙනවා (1, 2, 3...)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String role; // ROLE_ADMIN, ROLE_MEMBER, ROLE_TRAINER
}