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

    //all class take the list
    public List<GymClass> getAllClasses() {
        return classRepository.findAll();
    }

    // save the new class
    public void saveClass(GymClass gymClass) {
        classRepository.save(gymClass);
    }

    // ID find by class
    public GymClass getClassById(Long id) {
        return classRepository.findById(id).orElse(null);
    }

    public void deleteClass(Long id) {
        classRepository.deleteById(id);
    }
}