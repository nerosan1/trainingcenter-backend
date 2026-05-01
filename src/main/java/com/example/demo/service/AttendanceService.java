package com.example.demo.service;

import com.example.demo.dto.attendance.AttendanceResponse;
import com.example.demo.dto.attendance.AttendanceRecordDto;
import com.example.demo.entity.Attendance;
import com.example.demo.entity.AttendanceStatus;
import com.example.demo.entity.ClassSession;
import com.example.demo.entity.Enrollment;
import com.example.demo.entity.EnrollmentStatus;
import com.example.demo.entity.User;
import com.example.demo.repository.AuditLogRepository;
import com.example.demo.repository.AttendanceRepository;
import com.example.demo.repository.ClassSessionRepository;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final ClassSessionRepository sessionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, ClassSessionRepository sessionRepository,
            EnrollmentRepository enrollmentRepository, UserRepository userRepository,
            AuditLogRepository auditLogRepository) {
        this.attendanceRepository = attendanceRepository;
        this.sessionRepository = sessionRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public List<AttendanceResponse> getAttendanceBySession(Integer sessionId) {
        return attendanceRepository.findBySessionSessionId(sessionId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<AttendanceResponse> getAttendanceByStudent(Integer studentId) {
        return attendanceRepository.findByStudentUserId(studentId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<AttendanceResponse> getAttendanceByClass(Integer classId) {
        return attendanceRepository.findBySessionTrainingClassClassId(classId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<AttendanceResponse> markAttendance(Integer sessionId, List<AttendanceRecordDto> records,
            Integer actorId) {
        ClassSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        List<Attendance> attendances = new ArrayList<>();

        for (AttendanceRecordDto record : records) {
            User student = userRepository.findById(record.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found: " + record.getStudentId()));

            Attendance attendance = attendanceRepository.findAll().stream()
                    .filter(a -> a.getSession().getSessionId().equals(sessionId)
                            && a.getStudent().getUserId().equals(record.getStudentId()))
                    .findFirst()
                    .orElseGet(() -> {
                        Attendance a = new Attendance();
                        a.setSession(session);
                        a.setStudent(student);
                        return a;
                    });

            attendance.setStatus(AttendanceStatus.valueOf(record.getStatus().toUpperCase()));
            attendance.setNote(record.getNote());
            attendance.setMarkedAt(Instant.now());

            attendances.add(attendanceRepository.save(attendance));
        }

        logAction(actorId, "MARK_ATTENDANCE", "Session", sessionId);

        return attendances.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public AttendanceResponse markSingleAttendance(Integer sessionId, Integer studentId, AttendanceRecordDto record,
            Integer actorId) {
        ClassSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Attendance attendance = attendanceRepository.findAll().stream()
                .filter(a -> a.getSession().getSessionId().equals(sessionId)
                        && a.getStudent().getUserId().equals(studentId))
                .findFirst()
                .orElseGet(() -> {
                    Attendance a = new Attendance();
                    a.setSession(session);
                    a.setStudent(student);
                    return a;
                });

        attendance.setStatus(AttendanceStatus.valueOf(record.getStatus().toUpperCase()));
        attendance.setNote(record.getNote());
        attendance.setMarkedAt(Instant.now());

        Attendance saved = attendanceRepository.save(attendance);
        logAction(actorId, "MARK_SINGLE_ATTENDANCE", "Attendance", saved.getAttendanceId());

        return toResponse(saved);
    }

    @Transactional
    public AttendanceResponse updateAttendanceStatus(Integer attendanceId, String status, Integer actorId) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));

        attendance.setStatus(AttendanceStatus.valueOf(status.toUpperCase()));
        attendance.setMarkedAt(Instant.now());

        Attendance saved = attendanceRepository.save(attendance);
        logAction(actorId, "UPDATE_ATTENDANCE", "Attendance", saved.getAttendanceId());

        return toResponse(saved);
    }

    public double getAttendanceRate(Integer studentId, Integer classId) {
        List<Attendance> attendances = attendanceRepository.findBySessionTrainingClassClassId(classId).stream()
                .filter(a -> a.getStudent().getUserId().equals(studentId))
                .collect(Collectors.toList());

        if (attendances.isEmpty()) {
            return 0.0;
        }

        long presentCount = attendances.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.PRESENT || a.getStatus() == AttendanceStatus.LATE)
                .count();

        return (double) presentCount / attendances.size() * 100;
    }

    private void logAction(Integer userId, String action, String entity, Integer entityId) {
        if (userId != null) {
            try {
                var actor = userRepository.findById(userId).orElse(null);
                if (actor != null) {
                    var log = new com.example.demo.entity.AuditLog();
                    log.setUser(actor);
                    log.setAction(action);
                    log.setEntity(entity);
                    log.setEntityId(entityId);
                    auditLogRepository.save(log);
                }
            } catch (Exception ignored) {
            }
        }
    }

    private AttendanceResponse toResponse(Attendance attendance) {
        return new AttendanceResponse(
                attendance.getAttendanceId(),
                attendance.getSession().getSessionId(),
                attendance.getStudent().getUserId(),
                attendance.getStudent().getFullName(),
                attendance.getStatus().name(),
                attendance.getNote(),
                attendance.getMarkedAt());
    }
}
