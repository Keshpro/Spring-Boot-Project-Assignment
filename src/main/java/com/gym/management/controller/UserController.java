package com.gym.management.controller;

import com.gym.management.entity.User;
import com.gym.management.repository.PaymentRepository;
import com.gym.management.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
//    private final PaymentRepository paymentRepository;

    // Constructor එක හරහා Repository දෙකම සම්බන්ධ කිරීම
    public UserController(UserRepository userRepository, PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
    }

    // 1. User Dashboard
    @GetMapping("/dashboard")
    public String userDashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);

        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("attendanceCount", 12); // පසුව හදමු


            LocalDate today = LocalDate.now();


            LocalDate startDate = today.withDayOfMonth(1);

            LocalDate endDate = today.withDayOfMonth(today.lengthOfMonth());
            
            boolean isPaid = paymentRepository.existsByUserAndDateBetween(user, startDate, endDate);

            if (isPaid) {
                model.addAttribute("paymentStatus", "PAID");
            } else {
                model.addAttribute("paymentStatus", "OVERDUE");
            }
        }

        return "user-dashboard";
    }

    // 2. Profile Form
    @GetMapping("/profile")
    public String showProfileForm(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            model.addAttribute("user", user);
            return "user-profile";
        }
        return "redirect:/user/dashboard";
    }

    // 3. Update Profile Logic
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute User updatedUser, Authentication authentication) {
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username).orElse(null);

        if (currentUser != null) {
            currentUser.setAge(updatedUser.getAge());
            currentUser.setWeight(updatedUser.getWeight());
            currentUser.setHeight(updatedUser.getHeight());
            currentUser.setEmergencyContact(updatedUser.getEmergencyContact());
            currentUser.setHealthStatus(updatedUser.getHealthStatus());

            userRepository.save(currentUser);
        }
        return "redirect:/user/dashboard?updated";
    }
}