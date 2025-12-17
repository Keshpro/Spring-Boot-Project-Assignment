package com.gym.management.controller;

import com.gym.management.entity.Attendance;
import com.gym.management.entity.User;
import com.gym.management.repository.AttendanceRepository;
import com.gym.management.repository.PaymentRepository;
import com.gym.management.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
public class CheckInController {

    private final UserService userService; // User හොයන්න (අපේ පරණ Service එකේ findUserByUsername නැත්නම් Repository එක ගමු)
    // NOTE: UserService එකේ findByUsername නැත්නම් කෙලින්ම Repository එක දාගන්න පහළ විදියට:
    private final com.gym.management.repository.UserRepository userRepository;

    private final AttendanceRepository attendanceRepository;
    private final PaymentRepository paymentRepository;
    private final PasswordEncoder passwordEncoder;

    public CheckInController(com.gym.management.repository.UserRepository userRepository, AttendanceRepository attendanceRepository, PaymentRepository paymentRepository, PasswordEncoder passwordEncoder, UserService userService) {
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
        this.paymentRepository = paymentRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    // 1. QR Code එක Scan කළාම එන පිටුව (Login Form එක වගේ)
    @GetMapping("/checkin")
    public String showCheckInPage() {
        return "public-checkin"; // public-checkin.html පිටුව
    }

    // 2. User විස්තර ගැහුවම Attendance Mark කරන තැන
    @PostMapping("/checkin/mark")
    public String processCheckIn(@RequestParam String username, @RequestParam String password, Model model) {
        // A. User ව හොයනවා
        User user = userRepository.findByUsername(username).orElse(null);

        // B. User ඇත්තටම ඉන්නවද සහ Password හරිද බලනවා
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("error", "Invalid Username or Password!");
            return "public-checkin";
        }

        // C. Payment Check කරනවා (මේ මාසෙට ගෙවලාද?)
        String currentMonth = LocalDate.now().getMonth().toString() + " " + LocalDate.now().getYear(); // Example: JANUARY 2025
        // (Admin Payment දානකොට 'January 2025' වගේ දැම්මා නම් මේ Format එක බලන්න වෙනවා. අපි Case Insensitive වෙන්න පොඩි ට්‍රික් එකක් දාමු හෝ Payment එකේ Month එක හරියට දාන්න ඕන).

        // සරලව Payment Repository එකෙන් බලමු
        // (Payment එක දානකොට මාසෙ නම හරියටම වැටෙන්න ඕන. උදා: "JANUARY 2025")
        boolean isPaid = paymentRepository.existsByUserAndMonth(user, currentMonth);
        String status = isPaid ? "PAID ✅" : "NOT PAID ❌";

        // D. Attendance එක Save කරනවා
        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setDate(LocalDate.now());
        attendance.setTime(LocalTime.now());
        attendance.setPaymentStatus(status); // අලුත් කෑල්ල

        attendanceRepository.save(attendance);

        model.addAttribute("success", "Attendance Marked! Status: " + status);
        model.addAttribute("user", user.getUsername());
        return "public-checkin";
    }
}