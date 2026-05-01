package com.example.demo.service;

import com.example.demo.dto.enrollment.EnrollmentResponse;
import com.example.demo.dto.enrollment.TrainingClassDetailResponse;
import com.example.demo.dto.enrollment.CourseDetailResponse;
import com.example.demo.dto.enrollment.TeacherDetailResponse;
import com.example.demo.entity.Enrollment;
import com.example.demo.entity.EnrollmentStatus;
import com.example.demo.entity.TrainingClass;
import com.example.demo.entity.User;
import com.example.demo.repository.AuditLogRepository;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.TrainingClassRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final TrainingClassRepository classRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, TrainingClassRepository classRepository,
            UserRepository userRepository, AuditLogRepository auditLogRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.classRepository = classRepository;
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public EnrollmentResponse enrollClass(Integer classId, Integer studentId, Integer actorId) {
        var trainingClass = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));
        var student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Check if already enrolled
        boolean alreadyEnrolled = enrollmentRepository.findAll().stream()
                .anyMatch(e -> e.getTrainingClass().getClassId().equals(classId)
                        && e.getStudent().getUserId().equals(studentId)
                        && (e.getStatus() == EnrollmentStatus.PENDING || e.getStatus() == EnrollmentStatus.ACTIVE));

        if (alreadyEnrolled) {
            throw new RuntimeException("Already enrolled in this class");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setTrainingClass(trainingClass);
        enrollment.setStudent(student);
        enrollment.setStatus(EnrollmentStatus.PENDING);

        Enrollment saved = enrollmentRepository.save(enrollment);
        logAction(actorId, "ENROLL_CLASS", "Enrollment", saved.getEnrollmentId());

        return toResponse(saved);
    }

    public EnrollmentResponse getEnrollmentById(Integer id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        return toResponse(enrollment);
    }

    public List<EnrollmentResponse> getEnrollmentsByClass(Integer classId) {
        return enrollmentRepository.findByTrainingClassClassIdAndStatus(classId, null).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<EnrollmentResponse> getActiveEnrollmentsByClass(Integer classId) {
        return enrollmentRepository.findByTrainingClassClassIdAndStatus(classId, EnrollmentStatus.ACTIVE).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<EnrollmentResponse> getEnrollmentsByStudent(Integer studentId) {
        return enrollmentRepository.findByStudentUserId(studentId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

public List<EnrollmentResponse> getPendingEnrollmentsByClass(Integer classId) {
        return enrollmentRepository.findByTrainingClassClassIdAndStatus(classId, EnrollmentStatus.PENDING).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<EnrollmentResponse> getAllEnrollments() {
        return enrollmentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public EnrollmentResponse approveEnrollment(Integer enrollmentId, Integer actorId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        if (enrollment.getStatus() != EnrollmentStatus.PENDING) {
            throw new RuntimeException("Enrollment is not in PENDING status");
        }

        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        Enrollment saved = enrollmentRepository.save(enrollment);
        logAction(actorId, "APPROVE_ENROLLMENT", "Enrollment", saved.getEnrollmentId());

        // Notify student
        notificationServiceSimple(saved.getStudent().getUserId(), "Your enrollment has been approved!");

        return toResponse(saved);
    }

    @Transactional
    public EnrollmentResponse cancelEnrollment(Integer enrollmentId, Integer actorId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollment.setStatus(EnrollmentStatus.CANCELLED);
        Enrollment saved = enrollmentRepository.save(enrollment);
        logAction(actorId, "CANCEL_ENROLLMENT", "Enrollment", saved.getEnrollmentId());

        return toResponse(saved);
    }

    @Transactional
    public EnrollmentResponse completeEnrollment(Integer enrollmentId, Integer actorId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollment.setStatus(EnrollmentStatus.COMPLETED);
        Enrollment saved = enrollmentRepository.save(enrollment);
        logAction(actorId, "COMPLETE_ENROLLMENT", "Enrollment", saved.getEnrollmentId());

        return toResponse(saved);
    }

    public Enrollment getEnrollmentEntity(Integer id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
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

    private void notificationServiceSimple(Integer userId, String content) {
        try {
            var user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                var notification = new com.example.demo.entity.Notification();
                notification.setUser(user);
                notification.setContent(content);
                notification.setIsRead(false);
                var notificationRepo = com.example.demo.repository.NotificationRepository.class;
                // Will be injected
            }
        } catch (Exception ignored) {
        }
    }

    private EnrollmentResponse toResponse(Enrollment enrollment) {
        TrainingClass trainingClass = enrollment.getTrainingClass();
        
        // Build Course details
        CourseDetailResponse courseDetail = new CourseDetailResponse(
                trainingClass.getCourse().getCourseId(),
                trainingClass.getCourse().getCourseName(),
                trainingClass.getCourse().getDescription()
        );
        
        // Build Teacher details
        TeacherDetailResponse teacherDetail = new TeacherDetailResponse(
                trainingClass.getTeacher().getUserId(),
                trainingClass.getTeacher().getFullName(),
                trainingClass.getTeacher().getEmail()
        );
        
        // Build TrainingClass details
        TrainingClassDetailResponse trainingClassDetail = new TrainingClassDetailResponse(
                trainingClass.getClassId(),
                trainingClass.getCourse().getCourseName(),
                courseDetail,
                teacherDetail,
                trainingClass.getStartDate(),
                trainingClass.getEndDate(),
                trainingClass.getSchedule(),
                trainingClass.getRoom(),
                trainingClass.getStatus().name()
        );
        
        return new EnrollmentResponse(
                enrollment.getEnrollmentId(),
                enrollment.getStudent().getUserId(),
                enrollment.getStudent().getFullName(),
                enrollment.getTrainingClass().getClassId(),
                enrollment.getTrainingClass().getCourse().getCourseName(),
                enrollment.getStatus().name(),
                enrollment.getCreatedAt(),
                trainingClassDetail);
    }
}
