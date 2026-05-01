package com.example.demo.repository;

import com.example.demo.entity.TrainingClass;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TrainingClassRepository extends JpaRepository<TrainingClass, Integer> {
    List<TrainingClass> findByTeacherUserId(Integer teacherId);
}
