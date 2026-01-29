package com.gym.management.controller;

import com.gym.management.entity.Attendance;
import com.gym.management.entity.User;
import com.gym.management.repository.AttendanceRepository;
import com.gym.management.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
public class CheckInController {

    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;

    public CheckInController(UserRepository userRepository, AttendanceRepository attendanceRepository) {
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
    }

    // Check-in පිටුව පෙන්වීම
    @GetMapping("/checkin")
    public String showCheckInPage() {
        return "checkin";
    }

    // Attendance මාක් කිරීම
    @PostMapping("/checkin/mark")
    public String markAttendance(@RequestParam("identifier") String identifier) {
        // Phone Number එකෙන් හෝ Username එකෙන් User ව හොයනවා
        User user = userRepository.findByPhoneNumber(identifier).orElse(null);
        if (user == null) {
            user = userRepository.findByUsername(identifier).orElse(null);
        }

        if (user != null) {
            Attendance attendance = new Attendance();
            attendance.setUser(user);
            attendance.setDate(LocalDate.now());
            attendance.setTime(LocalTime.now());
            attendanceRepository.save(attendance);
            // සාර්ථක නම් නම පෙන්වන්න ID එකත් යවනවා (Optional)
            return "redirect:/checkin?success&name=" + user.getUsername();
        }
        return "redirect:/checkin?error";
    }
}