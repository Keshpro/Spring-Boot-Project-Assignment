package com.gym.management.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "attendance")
@Data
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // කවුද ආවේ?

    private LocalDate date; // ආපු දවස
    private LocalTime time; // ආපු වෙලාව

    private String paymentStatus; // "PAID" or "NOT PAID" කියලා සේව් කරන්න
    }
