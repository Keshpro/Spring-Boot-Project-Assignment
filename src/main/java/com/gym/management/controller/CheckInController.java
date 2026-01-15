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

    // 1. Check-In පිටුව පෙන්වීම
    @GetMapping("/checkin")
    public String showCheckInPage() {
        return "checkin"; // checkin.html පිටුව
    }

    // 2. Attendance Mark කිරීම (Phone Number හෝ Username මගින්)
    @PostMapping("/checkin/mark")
    public String markAttendance(@RequestParam("identifier") String identifier, Model model) {

        // මුලින්ම Phone Number එකෙන් බලනවා
        User user = userRepository.findByPhoneNumber(identifier).orElse(null);

        // Phone Number එකෙන් හම්බුනේ නැත්නම්, Username එකෙන් බලනවා
        if (user == null) {
            user = userRepository.findByUsername(identifier).orElse(null);
        }

        if (user != null) {
            // User ඉන්නවා නම් Attendance දානවා
            Attendance attendance = new Attendance();
            attendance.setUser(user);
            attendance.setDate(LocalDate.now());
            attendance.setTime(LocalTime.now());

            attendanceRepository.save(attendance);

            return "redirect:/checkin?success";
        } else {
            // User කෙනෙක් නෑ
            return "redirect:/checkin?error";
        }
    }
}