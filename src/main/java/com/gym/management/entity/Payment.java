package com.gym.management.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    // මේක තමයි අඩු වෙලා තිබුනේ
    private LocalDate date;

    // Dashboard එකේ මාසය අනුව පෙන්නන්න ඕන නිසා
    private String month;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // --- GETTERS AND SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    // ✅ මේ Method එක තමයි Error එකේ ඉල්ලන්නේ
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}