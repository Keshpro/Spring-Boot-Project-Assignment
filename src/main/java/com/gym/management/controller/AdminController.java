package com.gym.management.controller;

import com.gym.management.entity.User;
import com.gym.management.repository.AttendanceRepository;
import com.gym.management.repository.PaymentRepository;
import com.gym.management.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final PaymentRepository paymentRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository, AttendanceRepository attendanceRepository, PaymentRepository paymentRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
        this.paymentRepository = paymentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalMembers", userRepository.count());
        model.addAttribute("todayAttendance", attendanceRepository.countByDate(LocalDate.now()));

        // Income Calculations
        String currentMonth = LocalDate.now().getMonth().toString() + " " + LocalDate.now().getYear();
        Double monthlyIncome = paymentRepository.getTotalIncomeByMonth(currentMonth);
        model.addAttribute("monthlyIncome", monthlyIncome != null ? monthlyIncome : 0.0);

        return "admin-dashboard";
    }

    @GetMapping("/users")
    public String listUsers(Model model, @RequestParam(required = false) String keyword) {
        List<User> users;
        if (keyword != null && !keyword.isEmpty()) {
            users = userRepository.findByUsernameContainingIgnoreCase(keyword);
        } else {
            users = userRepository.findAll();
        }
        model.addAttribute("users", users);
        return "admin-users";
    }

    @GetMapping("/users/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin-user-add";
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute User user) {
        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "redirect:/admin/users/add?error";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("MEMBER");
        userRepository.save(user);
        return "redirect:/admin/users?success";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users?deleted";
    }

    @GetMapping("/attendance")
    public String viewAttendance(@RequestParam(required = false) LocalDate date, Model model) {

        if (date == null) {
            date = LocalDate.now();
        }

        model.addAttribute("selectedDate", date);
        model.addAttribute("attendanceList", attendanceRepository.findByDateOrderByTimeDesc(date));

        return "admin-attendance";
    }

    @GetMapping("/users/view/{id}")
    public String viewUserDetails(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            model.addAttribute("user", user);
            return "admin-user-view";
        }
        return "redirect:/admin/users";
    }
}