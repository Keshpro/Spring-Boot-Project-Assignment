package com.gym.management.repository;

import com.gym.management.entity.Payment;
import com.gym.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUser(User user);
    // විශේෂිත මාසයක මුළු ආදායම එකතු කරලා ගන්න
    @org.springframework.data.jpa.repository.Query("SELECT SUM(p.amount) FROM Payment p WHERE p.month = :month")
    Double getTotalIncomeByMonth(String month);

    // අද දවසේ ආදායම
    @org.springframework.data.jpa.repository.Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentDate = :today")
    Double getTotalIncomeToday(java.time.LocalDate today);
    void deleteByUser(User user); // User ට අදාළ පේමන්ට්ස් මකන්න
    // User කෙනෙක් අදාළ මාසෙට ගෙවලාද කියලා බලන්න
    boolean existsByUserAndMonth(User user, String month);
}