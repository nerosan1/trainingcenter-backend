package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.audit.AuditLogResponse;
import com.example.demo.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public ResponseEntity<?> getAllLogs() {
        try {
            List<AuditLogResponse> logs = auditLogService.getAllLogs();
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getLogsByUser(@PathVariable Integer userId) {
        try {
            List<AuditLogResponse> logs = auditLogService.getLogsByUser(userId);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/entity/{entity}")
    public ResponseEntity<?> getLogsByEntity(@PathVariable String entity) {
        try {
            List<AuditLogResponse> logs = auditLogService.getLogsByEntity(entity);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/entity/{entity}/{entityId}")
    public ResponseEntity<?> getLogsByEntityAndId(@PathVariable String entity, @PathVariable Integer entityId) {
        try {
            List<AuditLogResponse> logs = auditLogService.getLogsByEntityAndId(entity, entityId);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<?> getRecentLogs(@RequestParam(defaultValue = "50") int limit) {
        try {
            List<AuditLogResponse> logs = auditLogService.getRecentLogs(limit);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}
