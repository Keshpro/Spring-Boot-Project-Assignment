package com.gym.management.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // කවුද ගෙව්වේ?

    private Double amount; // කීයක් ගෙව්වද?
    private String month; // මොන මාසෙටද? (January 2025)
    private LocalDate paymentDate; // ගෙවපු දවස
}