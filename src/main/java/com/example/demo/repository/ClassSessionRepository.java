package com.example.demo.repository;

import com.example.demo.entity.ClassSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

public interface ClassSessionRepository extends JpaRepository<ClassSession, Integer> {
    Optional<ClassSession> findByTrainingClassClassIdAndSessionDate(Integer classId, LocalDate sessionDate);
    List<ClassSession> findByTrainingClassClassId(Integer classId);
}
