package com.gym.management.controller;

import com.gym.management.entity.Attendance;
import com.gym.management.entity.User;
import com.gym.management.repository.AttendanceRepository;
import com.gym.management.repository.UserRepository;
import com.gym.management.service.UserPdfExporter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AttendanceRepository attendanceRepository;

    public AdminController(UserRepository userRepository, PasswordEncoder passwordEncoder, AttendanceRepository attendanceRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.attendanceRepository = attendanceRepository;
    }

    // 1. Dashboard
    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "admin-dashboard";
    }

    // 2. User List
    @GetMapping("/users")
    public String listUsers(Model model, @RequestParam(required = false) String keyword) {
        List<User> users;
        if (keyword != null && !keyword.isEmpty()) {
            users = userRepository.findByUsernameContainingIgnoreCase(keyword);
            model.addAttribute("keyword", keyword);
        } else {
            users = userRepository.findAll();
        }
        model.addAttribute("users", users);
        return "admin-users";
    }

    // 3. Add User Form
    @GetMapping("/users/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin-user-add";
    }

    // 4. Save User
    @PostMapping("/users/save")
    public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin-user-add";
        }
        if (userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            result.rejectValue("phoneNumber", "error.user", "Phone number is already registered");
            return "admin-user-add";
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            result.rejectValue("username", "error.user", "Username is already taken");
            return "admin-user-add";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
        return "redirect:/admin/users?success";
    }

    // 5. Edit User Form
    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            model.addAttribute("user", user);
            return "admin-user-edit";
        }
        return "redirect:/admin/users";
    }

    // 6. Update User
    @PostMapping("/users/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setUsername(user.getUsername());
            existingUser.setPhoneNumber(user.getPhoneNumber());
            existingUser.setRole(user.getRole()); // Role එකත් update වෙනවා

            // Password එක හිස් නැත්නම් විතරක් Update කරනවා
            // (Frontend එකේ newPassword කියලා field එකක් නැති නිසා මේක සරලව තියමු)

            userRepository.save(existingUser);
        }
        return "redirect:/admin/users?success";
    }

    // 7. Delete User (මේක තමයි වැඩ කළේ නැත්තේ)
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users?deleted";
    }

    // 8. Attendance Mark (මේකත් වැඩ කළේ නැත්තේ මේ කොටස නැති නිසා)
    @GetMapping("/users/attendance/{id}")
    public String markAttendance(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            Attendance attendance = new Attendance();
            attendance.setUser(user);
            attendance.setDate(LocalDate.now());
            attendance.setTime(LocalTime.now());
            attendanceRepository.save(attendance);
        }
        return "redirect:/admin/users?attendanceMarked";
    }

    // 9. PDF Export
    @GetMapping("/users/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        List<User> listUsers = userRepository.findAll();
        UserPdfExporter exporter = new UserPdfExporter(listUsers);
        exporter.export(response);
    }
    // --- Detailed Report: Today's Attendance ---
    @GetMapping("/attendance/today")
    public String viewTodayAttendance(Model model) {
        // අද දිනය
        LocalDate today = LocalDate.now();

        // අද දවසේ ආපු අයගේ ලිස්ට් එක ගන්නවා
        List<Attendance> todayList = attendanceRepository.findByDate(today);

        model.addAttribute("attendanceList", todayList);
        model.addAttribute("reportDate", today); // දිනය පෙන්නන්න ඕන නිසා

        return "admin-attendance-today"; // අලුත් HTML පිටුව
    }
}