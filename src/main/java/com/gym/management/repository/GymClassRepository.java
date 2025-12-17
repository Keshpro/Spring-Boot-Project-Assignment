package com.gym.management.repository;

import com.gym.management.entity.GymClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymClassRepository extends JpaRepository<GymClass, Long> {
    // දැනට විශේෂ මෙතඩ් ඕන නැහැ, CRUD ඔක්කොම JpaRepository එකෙන් ලැබෙනවා.
}