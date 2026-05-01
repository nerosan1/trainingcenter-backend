package com.example.demo.repository;

import com.example.demo.entity.Enrollment;
import com.example.demo.entity.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    List<Enrollment> findByTrainingClassClassIdAndStatus(Integer classId, EnrollmentStatus status);
    List<Enrollment> findByStudentUserId(Integer studentId);
    Optional<Enrollment> findByEnrollmentIdAndStatus(Integer enrollmentId, EnrollmentStatus status);
}
