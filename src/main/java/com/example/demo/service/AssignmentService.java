package com.example.demo.service;

import com.example.demo.dto.assignment.AssignmentResponse;
import com.example.demo.dto.assignment.CreateAssignmentRequest;
import com.example.demo.entity.Assignment;
import com.example.demo.entity.TrainingClass;
import com.example.demo.repository.AuditLogRepository;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.repository.TrainingClassRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final TrainingClassRepository classRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;

    public AssignmentService(AssignmentRepository assignmentRepository, TrainingClassRepository classRepository,
            UserRepository userRepository, AuditLogRepository auditLogRepository) {
        this.assignmentRepository = assignmentRepository;
        this.classRepository = classRepository;
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public AssignmentResponse createAssignment(CreateAssignmentRequest request, Integer actorId) {
        TrainingClass trainingClass = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found"));

        Assignment assignment = new Assignment();
        assignment.setTrainingClass(trainingClass);
        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setDueDate(request.getDueDate());

        Assignment saved = assignmentRepository.save(assignment);
        logAction(actorId, "CREATE_ASSIGNMENT", "Assignment", saved.getAssignmentId());

        return toResponse(saved);
    }

    public AssignmentResponse getAssignmentById(Integer id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        return toResponse(assignment);
    }

    public List<AssignmentResponse> getAssignmentsByClass(Integer classId) {
        return assignmentRepository.findByTrainingClassClassId(classId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<AssignmentResponse> getUpcomingAssignments(Integer classId) {
        LocalDate today = LocalDate.now();
        return assignmentRepository.findByTrainingClassClassId(classId).stream()
                .filter(a -> a.getDueDate().isAfter(today) || a.getDueDate().equals(today))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AssignmentResponse updateAssignment(Integer id, CreateAssignmentRequest request, Integer actorId) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (request.getTitle() != null) {
            assignment.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            assignment.setDescription(request.getDescription());
        }
        if (request.getDueDate() != null) {
            assignment.setDueDate(request.getDueDate());
        }

        Assignment saved = assignmentRepository.save(assignment);
        logAction(actorId, "UPDATE_ASSIGNMENT", "Assignment", saved.getAssignmentId());

        return toResponse(saved);
    }

    @Transactional
    public void deleteAssignment(Integer id, Integer actorId) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        assignmentRepository.delete(assignment);
        logAction(actorId, "DELETE_ASSIGNMENT", "Assignment", id);
    }

    public Assignment getAssignmentEntity(Integer id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
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

    private AssignmentResponse toResponse(Assignment assignment) {
        return new AssignmentResponse(
                assignment.getAssignmentId(),
                assignment.getTrainingClass().getClassId(),
                assignment.getTrainingClass().getCourse().getCourseName(),
                assignment.getTitle(),
                assignment.getDescription(),
                assignment.getDueDate());
    }
}
