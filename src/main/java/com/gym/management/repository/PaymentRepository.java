package com.gym.management.repository;

import com.gym.management.entity.Payment;
import com.gym.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.month = :month")
    Double getTotalIncomeByMonth(@Param("month") String month);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.date = :date")
    Double getTotalIncomeToday(@Param("date") LocalDate date);

    boolean existsByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
}