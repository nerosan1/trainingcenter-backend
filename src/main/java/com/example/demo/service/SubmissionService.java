package com.example.demo.service;

import com.example.demo.dto.submission.CreateSubmissionRequest;
import com.example.demo.dto.submission.SubmissionResponse;
import com.example.demo.entity.Assignment;
import com.example.demo.entity.Submission;
import com.example.demo.entity.User;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.repository.AuditLogRepository;
import com.example.demo.repository.SubmissionRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;

    public SubmissionService(SubmissionRepository submissionRepository, AssignmentRepository assignmentRepository,
            UserRepository userRepository, AuditLogRepository auditLogRepository) {
        this.submissionRepository = submissionRepository;
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public SubmissionResponse createSubmission(CreateSubmissionRequest request, Integer studentId, Integer actorId) {
        Assignment assignment = assignmentRepository.findById(request.getAssignmentId())
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setFileUrl(request.getFileUrl());

        Submission saved = submissionRepository.save(submission);
        logAction(actorId, "CREATE_SUBMISSION", "Submission", saved.getSubmissionId());

        return toResponse(saved);
    }

    public SubmissionResponse getSubmissionById(Integer id) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        return toResponse(submission);
    }

    public List<SubmissionResponse> getSubmissionsByAssignment(Integer assignmentId) {
        return submissionRepository.findByAssignmentAssignmentId(assignmentId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<SubmissionResponse> getSubmissionsByStudent(Integer studentId) {
        return submissionRepository.findByStudentUserId(studentId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public SubmissionResponse gradeSubmission(Integer submissionId, Float grade, String feedback, Integer actorId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        submission.setGrade(grade);
        submission.setFeedback(feedback);

        Submission saved = submissionRepository.save(submission);
        logAction(actorId, "GRADE_SUBMISSION", "Submission", saved.getSubmissionId());

        // Notify student
        sendNotificationToUser(saved.getStudent().getUserId(),
                "Your submission for '" + saved.getAssignment().getTitle() + "' has been graded: " + grade + "/100");

        return toResponse(saved);
    }

    @Transactional
    public SubmissionResponse updateSubmission(Integer submissionId, String fileUrl, Integer actorId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        submission.setFileUrl(fileUrl);

        Submission saved = submissionRepository.save(submission);
        logAction(actorId, "UPDATE_SUBMISSION", "Submission", saved.getSubmissionId());

        return toResponse(saved);
    }

    @Transactional
    public void deleteSubmission(Integer id, Integer actorId) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        submissionRepository.delete(submission);
        logAction(actorId, "DELETE_SUBMISSION", "Submission", id);
    }

    public Submission getSubmissionEntity(Integer id) {
        return submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
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

    private void sendNotificationToUser(Integer userId, String content) {
        try {
            var user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                var notification = new com.example.demo.entity.Notification();
                notification.setUser(user);
                notification.setContent(content);
                notification.setIsRead(false);
                var repo = com.example.demo.repository.NotificationRepository.class;
            }
        } catch (Exception ignored) {
        }
    }

    private SubmissionResponse toResponse(Submission submission) {
        return new SubmissionResponse(
                submission.getSubmissionId(),
                submission.getAssignment().getAssignmentId(),
                submission.getAssignment().getTitle(),
                submission.getStudent().getUserId(),
                submission.getStudent().getFullName(),
                submission.getFileUrl(),
                submission.getGrade(),
                submission.getFeedback(),
                submission.getSubmittedAt());
    }
}
