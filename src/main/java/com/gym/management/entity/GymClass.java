package com.gym.management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gym_classes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GymClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // පන්තියේ නම (Yoga, Zumba)

    private String instructor; // උගන්වන කෙනාගේ නම

    private String scheduleTime; // වෙලාව (Monday 8 AM)

    private int maxCapacity; // උපරිම සහභාගී විය හැකි ගණන
}