package com.gym.management.controller;

import com.gym.management.entity.Payment;
import com.gym.management.entity.User;
import com.gym.management.repository.PaymentRepository;
import com.gym.management.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/payments")
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    public PaymentController(PaymentRepository paymentRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    // 1. Payment List (ඉතිහාසය පෙන්වීම)
    @GetMapping
    public String listPayments(Model model) {
        model.addAttribute("payments", paymentRepository.findAll());
        return "admin-payments";
    }

    // 2. Add Payment Form (ගෙවීම් පිටුවට යාම)
    @GetMapping("/add/{userId}")
    public String showAddPaymentForm(@PathVariable Long userId, Model model) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Payment payment = new Payment();
            payment.setUser(user);
            model.addAttribute("payment", payment);
            model.addAttribute("user", user);
            return "admin-payment-add";
        }
        return "redirect:/admin/users";
    }

    // 3. Save Payment (සල්ලි ගෙවීම සේව් කිරීම)
    @PostMapping("/save")
    public String savePayment(@ModelAttribute Payment payment, @RequestParam("userId") Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            payment.setUser(user);
            payment.setDate(LocalDate.now());
            // මාසය සෙට් කිරීම (Dashboard එකට)
            String currentMonth = LocalDate.now().getMonth().toString() + " " + LocalDate.now().getYear();
            payment.setMonth(currentMonth);

            paymentRepository.save(payment);
        }
        return "redirect:/admin/payments?success";
    }

    // 4. Delete Payment (ගෙවීමක් මකා දැමීම)
    @GetMapping("/delete/{id}")
    public String deletePayment(@PathVariable Long id) {
        paymentRepository.deleteById(id);
        return "redirect:/admin/payments?deleted";
    }
}