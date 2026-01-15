package com.gym.management.controller;

import com.gym.management.entity.GymClass;
import com.gym.management.repository.ClassRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/classes")
public class ClassController {

    private final ClassRepository classRepository;

    public ClassController(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    // 1. List Classes (ඔක්කොම පන්ති පෙන්වීම)
    @GetMapping
    public String listClasses(Model model) {
        List<GymClass> classes = classRepository.findAll();
        model.addAttribute("classes", classes);
        return "admin-classes";
    }

    // 2. Add Class Form (මේ කොටස අනිවාර්යයෙන්ම තියෙන්න ඕන)
    @GetMapping("/add")
    public String showAddClassForm(Model model) {
        model.addAttribute("gymClass", new GymClass());
        return "admin-class-add";
    }

    // 3. Save Class (Date එකේ T අකුර අයින් කරලා Save කරන තැන)
    @PostMapping("/save")
    public String saveClass(@ModelAttribute GymClass gymClass) {

        // Date Format Fix (මැදට එන 'T' අකුර අයින් කරලා Space එකක් දානවා)
        if (gymClass.getSchedule() != null) {
            String formattedDate = gymClass.getSchedule().replace("T", " ");
            gymClass.setSchedule(formattedDate);
        }

        classRepository.save(gymClass);
        return "redirect:/admin/classes?success";
    }

    // 4. Delete Class
    @GetMapping("/delete/{id}")
    public String deleteClass(@PathVariable Long id) {
        classRepository.deleteById(id);
        return "redirect:/admin/classes?deleted";
    }
}