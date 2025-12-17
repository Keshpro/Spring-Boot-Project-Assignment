package com.gym.management.repository;

import com.gym.management.entity.Attendance;
import com.gym.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByUser(User user);

    long countByDate(java.time.LocalDate date);

    // අලුතින් දැම්මේ මේක (දවසක් දුන්නම එදා ආපු අයගේ ලිස්ට් එක ගන්න)
    List<Attendance> findByDate(java.time.LocalDate date);
    void deleteByUser(User user); // User ට අදාළ පැමිණීම් මකන්න
}

