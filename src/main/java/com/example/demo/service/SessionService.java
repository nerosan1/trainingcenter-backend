package com.example.demo.service;

import com.example.demo.dto.session.CreateSessionRequest;
import com.example.demo.dto.session.SessionResponse;
import com.example.demo.entity.ClassSession;
import com.example.demo.entity.TrainingClass;
import com.example.demo.repository.AuditLogRepository;
import com.example.demo.repository.ClassSessionRepository;
import com.example.demo.repository.TrainingClassRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionService {

    private final ClassSessionRepository sessionRepository;
    private final TrainingClassRepository classRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;

    public SessionService(ClassSessionRepository sessionRepository, TrainingClassRepository classRepository,
            UserRepository userRepository, AuditLogRepository auditLogRepository) {
        this.sessionRepository = sessionRepository;
        this.classRepository = classRepository;
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public SessionResponse createSession(CreateSessionRequest request, Integer actorId) {
        TrainingClass trainingClass = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found"));

        ClassSession session = new ClassSession();
        session.setTrainingClass(trainingClass);
        session.setSessionDate(LocalDate.parse(request.getSessionDate()));
        session.setStartTime(LocalTime.parse(request.getStartTime()));
        session.setEndTime(LocalTime.parse(request.getEndTime()));
        session.setTopic(request.getTopic());

        ClassSession saved = sessionRepository.save(session);
        logAction(actorId, "CREATE_SESSION", "Session", saved.getSessionId());

        return toResponse(saved);
    }

    public SessionResponse getSessionById(Integer id) {
        ClassSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        return toResponse(session);
    }

    public List<SessionResponse> getSessionsByClass(Integer classId) {
        return sessionRepository.findByTrainingClassClassId(classId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public SessionResponse getTodaySession(Integer classId) {
        LocalDate today = LocalDate.now();
        return sessionRepository.findByTrainingClassClassIdAndSessionDate(classId, today)
                .map(this::toResponse)
                .orElse(null);
    }

    @Transactional
    public SessionResponse getOrCreateTodaySession(Integer classId, Integer actorId) {
        LocalDate today = LocalDate.now();

        var existing = sessionRepository.findByTrainingClassClassIdAndSessionDate(classId, today);
        if (existing.isPresent()) {
            return toResponse(existing.get());
        }

        // Create today's session with default times
        TrainingClass trainingClass = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        ClassSession session = new ClassSession();
        session.setTrainingClass(trainingClass);
        session.setSessionDate(today);
        session.setStartTime(LocalTime.of(9, 0));
        session.setEndTime(LocalTime.of(12, 0));
        session.setTopic("Daily Session");

        ClassSession saved = sessionRepository.save(session);
        logAction(actorId, "CREATE_TODAY_SESSION", "Session", saved.getSessionId());

        return toResponse(saved);
    }

    @Transactional
    public SessionResponse updateSession(Integer id, CreateSessionRequest request, Integer actorId) {
        ClassSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (request.getSessionDate() != null) {
            session.setSessionDate(LocalDate.parse(request.getSessionDate()));
        }
        if (request.getStartTime() != null) {
            session.setStartTime(LocalTime.parse(request.getStartTime()));
        }
        if (request.getEndTime() != null) {
            session.setEndTime(LocalTime.parse(request.getEndTime()));
        }
        if (request.getTopic() != null) {
            session.setTopic(request.getTopic());
        }

        ClassSession saved = sessionRepository.save(session);
        logAction(actorId, "UPDATE_SESSION", "Session", saved.getSessionId());

        return toResponse(saved);
    }

    @Transactional
    public void deleteSession(Integer id, Integer actorId) {
        ClassSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        sessionRepository.delete(session);
        logAction(actorId, "DELETE_SESSION", "Session", id);
    }

    public ClassSession getSessionEntity(Integer id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));
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

    private SessionResponse toResponse(ClassSession session) {
        return new SessionResponse(
                session.getSessionId(),
                session.getTrainingClass().getClassId(),
                session.getSessionDate(),
                session.getStartTime(),
                session.getEndTime(),
                session.getTopic());
    }
}
