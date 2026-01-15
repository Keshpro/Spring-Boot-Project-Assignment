package com.gym.management.controller;

import com.gym.management.entity.Payment;
import com.gym.management.entity.User;
import com.gym.management.repository.PaymentRepository;
import com.gym.management.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/payments")
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    public PaymentController(PaymentRepository paymentRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    // 1. Payment History (ගෙවීම් ඉතිහාසය පෙන්වීම)
    @GetMapping
    public String listPayments(Model model) {
        List<Payment> payments = paymentRepository.findAll();
        model.addAttribute("payments", payments);
        return "admin-payments"; // මේ HTML එක අපි ඊළඟට හදමු
    }

    // 2. Add Payment Form (ගෙවීම් කරන ෆෝම් එක පෙන්වීම)
    @GetMapping("/add/{userId}")
    public String showAddPaymentForm(@PathVariable Long userId, Model model) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Payment payment = new Payment();
            payment.setUser(user); // අදාල User ව Payment එකට සෙට් කරනවා
            model.addAttribute("payment", payment);
            model.addAttribute("user", user);
            return "admin-payment-add"; // මේ HTML එකත් අපි හදමු
        }
        return "redirect:/admin/users";
    }

    // 3. Save Payment (ගෙවීම සේව් කිරීම)
    @PostMapping("/save")
    public String savePayment(@ModelAttribute Payment payment, @RequestParam("userId") Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            payment.setUser(user);
            payment.setDate(LocalDate.now()); // අද දිනය දානවා

            // මාසය සෙට් කරනවා (Dashboard එකට ඕන නිසා)
            String currentMonth = LocalDate.now().getMonth().toString() + " " + LocalDate.now().getYear();
            payment.setMonth(currentMonth);

            paymentRepository.save(payment);
        }
        return "redirect:/admin/users?paymentSuccess";
    }

    // 4. Delete Payment (වැරදි ගෙවීමක් මකන්න ඕන නම්)
    @GetMapping("/delete/{id}")
    public String deletePayment(@PathVariable Long id) {
        paymentRepository.deleteById(id);
        return "redirect:/admin/payments?deleted";
    }
}