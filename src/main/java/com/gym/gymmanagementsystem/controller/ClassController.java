package com.gym.management.controller;

import com.gym.management.entity.GymClass;
import com.gym.management.service.ClassService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable; // මේක තමයි අඩුවෙලා තිබ්බේ
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ClassController {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    // 1. තියෙන ඔක්කොම Classes පෙන්වන පිටුව
    @GetMapping("/classes")
    public String listClasses(Model model) {
        model.addAttribute("classes", classService.getAllClasses());
        return "classes";
    }

    // 2. අලුත් Class එකක් දාන Form එක පෙන්වන්න
    @GetMapping("/classes/add")
    public String showAddClassForm(Model model) {
        model.addAttribute("gymClass", new GymClass());
        return "add-class";
    }

    // 3. Form එක Submit කළාම Save කරන කොටස
    @PostMapping("/classes/add")
    public String addClass(@ModelAttribute GymClass gymClass) {
        classService.saveClass(gymClass);
        return "redirect:/classes";
    }

    // 4. Edit Form එක පෙන්වීම
    @GetMapping("/classes/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        GymClass gymClass = classService.getClassById(id);
        model.addAttribute("gymClass", gymClass);
        return "edit-class";
    }

    // 5. Edit කරපු විස්තර Save කිරීම
    @PostMapping("/classes/update/{id}")
    public String updateClass(@PathVariable Long id, @ModelAttribute GymClass gymClass) {
        gymClass.setId(id); // පරණ ID එකම තියාගන්නවා
        classService.saveClass(gymClass);
        return "redirect:/classes";
    }

    // 6. Delete කිරීම
    @GetMapping("/classes/delete/{id}")
    public String deleteClass(@PathVariable Long id) {
        classService.deleteClass(id);
        return "redirect:/classes";
    }
}