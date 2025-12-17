package com.gym.management.service;

import com.gym.management.entity.GymClass;
import com.gym.management.repository.GymClassRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClassService {

    private final GymClassRepository classRepository;

    public ClassService(GymClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    // තියෙන ඔක්කොම Classes ලිස්ට් එකක් විදියට ගන්න
    public List<GymClass> getAllClasses() {
        return classRepository.findAll();
    }

    // අලුත් Class එකක් Save කරන්න
    public void saveClass(GymClass gymClass) {
        classRepository.save(gymClass);
    }

    // ID එකෙන් Class එකක් හොයාගන්න (Booking දානකොට ඕන වෙනවා)
    public GymClass getClassById(Long id) {
        return classRepository.findById(id).orElse(null);
    }
    // ID එක දුන්නම Class එක මකන මෙතඩ් එක
    public void deleteClass(Long id) {
        classRepository.deleteById(id);
    }
}