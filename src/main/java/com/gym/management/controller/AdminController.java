package com.gym.management.controller;

import com.gym.management.entity.Attendance;
import com.gym.management.entity.Payment;
import com.gym.management.entity.User;
import com.gym.management.repository.AttendanceRepository;
import com.gym.management.repository.PaymentRepository;
import com.gym.management.repository.UserRepository; // 1. මේක අලුතින් දැම්මා
import com.gym.management.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final AttendanceRepository attendanceRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository; // 2. Repository එක මෙතනට සම්බන්ධ කළා

    // Constructor එක update කළා
    public AdminController(UserService userService, AttendanceRepository attendanceRepository, PaymentRepository paymentRepository, UserRepository userRepository) {
        this.userService = userService;
        this.attendanceRepository = attendanceRepository;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    // 1. User List එක පෙන්වන පිටුව (Search එක්ක)
    @GetMapping("/users")
    public String viewUsers(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        List<User> list;

        if (keyword != null && !keyword.isEmpty()) {
            // සර්ච් එකක් කරලා නම්, ඒ අය විතරක් පෙන්නන්න
            list = userRepository.findByUsernameContainingIgnoreCase(keyword);
        } else {
            // නැත්නම් ඔක්කොම පෙන්නන්න
            list = userService.getAllUsers();
        }

        model.addAttribute("users", list);
        model.addAttribute("keyword", keyword); // සර්ච් කරපු වචනේ ආපහු පෙන්නන්න
        return "admin-users";
    }

    // 2. Attendance Mark කරන කොටස
    @GetMapping("/attendance/mark/{userId}")
    public String markAttendance(@PathVariable Long userId) {
        User user = userService.getUserById(userId);

        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setDate(LocalDate.now());
        attendance.setTime(LocalTime.now());

        attendanceRepository.save(attendance);
        return "redirect:/admin/users?success=AttendanceMarked";
    }

    // 3. Payment Page එක පෙන්වන කොටස
    @GetMapping("/payments/{userId}")
    public String viewPayments(@PathVariable Long userId, Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("payments", paymentRepository.findByUser(user));
        return "admin-payments";
    }

    // 4. අලුත් Payment එකක් Add කරන කොටස
    @PostMapping("/payments/add")
    public String addPayment(@RequestParam Long userId, @RequestParam Double amount, @RequestParam String month) {
        User user = userService.getUserById(userId);

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setAmount(amount);
        payment.setMonth(LocalDate.now().getMonth().toString() + " " + LocalDate.now().getYear());

        paymentRepository.save(payment);
        return "redirect:/admin/payments/" + userId;
    }

    // --- REPORTS SECTION ---



    @GetMapping("/reports/attendance")
    public String todayAttendanceReport(Model model) {
        model.addAttribute("attendances", attendanceRepository.findByDate(LocalDate.now()));
        model.addAttribute("reportTitle", "Today's Attendance (" + LocalDate.now() + ")");
        return "report-attendance";
    }

    @GetMapping("/reports/payments")
    public String paymentHistoryReport(Model model) {
        model.addAttribute("payments", paymentRepository.findAll());
        return "report-payments";
    }

    // --- EDIT & DELETE SECTION ---

    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "edit-user";
    }

    @PostMapping("/users/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user) {
        User existingUser = userService.getUserById(id);
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setRole(user.getRole());
        userService.saveUser(existingUser);
        return "redirect:/admin/users?success=UserUpdated";
    }

    // 3. User කෙනෙක් Delete කිරීම (දැන් වැඩ කරයි)
    @GetMapping("/users/delete/{id}")
    @Transactional
    public String deleteUser(@PathVariable Long id) {
        User user = userService.getUserById(id);

        // මුලින්ම එයාගේ Payments සහ Attendance මකනවා
        paymentRepository.deleteByUser(user);
        attendanceRepository.deleteByUser(user);

        // අන්තිමට User ව මකනවා (අපි අලුතින් දාපු repository එක පාවිච්චි කරලා)
        userRepository.delete(user);

        return "redirect:/admin/users?success=UserDeleted";
    }
    // PDF Export Method එක
    @GetMapping("/users/export/pdf")
    public void exportToPDF(jakarta.servlet.http.HttpServletResponse response) throws java.io.IOException {
        response.setContentType("application/pdf");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_list.pdf";
        response.setHeader(headerKey, headerValue);

        // Users ලා ඔක්කොම ගන්නවා
        List<User> listUsers = userService.getAllUsers();

        // PDF එක හදනවා
        com.gym.management.service.UserPdfExporter exporter = new com.gym.management.service.UserPdfExporter(listUsers);
        exporter.export(response);
    }
}
