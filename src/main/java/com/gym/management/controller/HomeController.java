package com.gym.management.controller;

import com.gym.management.entity.User;
import com.gym.management.repository.AttendanceRepository;
import com.gym.management.repository.PaymentRepository;
import com.gym.management.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class HomeController {

    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final PaymentRepository paymentRepository;
    private final PasswordEncoder passwordEncoder;

    public HomeController(UserRepository userRepository, AttendanceRepository attendanceRepository, PaymentRepository paymentRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
        this.paymentRepository = paymentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalMembers", userRepository.count());
        model.addAttribute("todayAttendance", attendanceRepository.countByDate(LocalDate.now()));

        String currentMonth = LocalDate.now().getMonth().toString() + " " + LocalDate.now().getYear();
        Double monthlyIncome = paymentRepository.getTotalIncomeByMonth(currentMonth);
        model.addAttribute("monthlyIncome", monthlyIncome != null ? monthlyIncome : 0.0);

        Double todayIncome = paymentRepository.getTotalIncomeToday(LocalDate.now());
        model.addAttribute("todayIncome", todayIncome != null ? todayIncome : 0.0);

        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // 🛑 NOTE: මෙතන තිබ්බ Register Methods දෙකම අයින් කළා.

    // --- PROFILE SECTION ---
    @GetMapping("/profile")
    public String viewProfile(Model model, java.security.Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute User user, @RequestParam(required = false) String newPassword, java.security.Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName()).orElse(null);

        if (currentUser != null) {
            currentUser.setUsername(user.getUsername());
            currentUser.setPhoneNumber(user.getPhoneNumber());

            if (newPassword != null && !newPassword.isEmpty()) {
                currentUser.setPassword(passwordEncoder.encode(newPassword));
            }

            userRepository.save(currentUser);
        }
        return "redirect:/profile?success";
    }
}