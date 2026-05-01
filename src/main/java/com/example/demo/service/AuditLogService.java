package com.example.demo.service;

import com.example.demo.dto.audit.AuditLogResponse;
import com.example.demo.entity.AuditLog;
import com.example.demo.entity.User;
import com.example.demo.repository.AuditLogRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public AuditLogService(AuditLogRepository auditLogRepository, UserRepository userRepository) {
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    public List<AuditLogResponse> getAllLogs() {
        return auditLogRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<AuditLogResponse> getLogsByUser(Integer userId) {
        return auditLogRepository.findByUserUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<AuditLogResponse> getLogsByEntity(String entity) {
        return auditLogRepository.findAll().stream()
                .filter(log -> log.getEntity().equalsIgnoreCase(entity))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<AuditLogResponse> getLogsByEntityAndId(String entity, Integer entityId) {
        return auditLogRepository.findAll().stream()
                .filter(log -> log.getEntity().equalsIgnoreCase(entity)
                        && log.getEntityId().equals(entityId))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<AuditLogResponse> getRecentLogs(int limit) {
        return auditLogRepository.findAll().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(limit)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private AuditLogResponse toResponse(AuditLog log) {
        return new AuditLogResponse(
                log.getLogId(),
                log.getUser().getUserId(),
                log.getAction(),
                log.getEntity(),
                log.getEntityId(),
                log.getCreatedAt());
    }
}
