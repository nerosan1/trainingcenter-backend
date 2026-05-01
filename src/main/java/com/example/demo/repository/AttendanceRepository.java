package com.example.demo.repository;

import com.example.demo.entity.Attendance;
import com.example.demo.entity.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findBySessionSessionId(Integer sessionId);
    List<Attendance> findByStudentUserId(Integer studentId);
    List<Attendance> findBySessionTrainingClassClassId(Integer classId);
    List<Attendance> findBySessionTrainingClassClassIdAndStatus(Integer classId, AttendanceStatus status);
}
