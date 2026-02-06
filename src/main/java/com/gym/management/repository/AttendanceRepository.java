package com.gym.management.repository;

import com.gym.management.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    //  (Newest first)
    List<Attendance> findByDateOrderByTimeDesc(LocalDate date);

    Long countByDate(LocalDate date);
}
