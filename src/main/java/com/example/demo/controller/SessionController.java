package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.session.CreateSessionRequest;
import com.example.demo.dto.session.SessionResponse;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public ResponseEntity<?> createSession(@Valid @RequestBody CreateSessionRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Integer actorId = userDetails != null ? userDetails.getUserId() : null;
            SessionResponse response = sessionService.createSession(request, actorId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSessionById(@PathVariable Integer id) {
        try {
            SessionResponse response = sessionService.getSessionById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<?> getSessionsByClass(@PathVariable Integer classId) {
        try {
            List<SessionResponse> sessions = sessionService.getSessionsByClass(classId);
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/class/{classId}/today")
    public ResponseEntity<?> getTodaySession(@PathVariable Integer classId) {
        try {
            SessionResponse response = sessionService.getTodaySession(classId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/class/{classId}/today-or-create")
    public ResponseEntity<?> getOrCreateTodaySession(@PathVariable Integer classId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Integer actorId = userDetails != null ? userDetails.getUserId() : null;
            SessionResponse response = sessionService.getOrCreateTodaySession(classId, actorId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSession(@PathVariable Integer id,
            @RequestBody CreateSessionRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Integer actorId = userDetails != null ? userDetails.getUserId() : null;
            SessionResponse response = sessionService.updateSession(id, request, actorId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSession(@PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Integer actorId = userDetails != null ? userDetails.getUserId() : null;
            sessionService.deleteSession(id, actorId);
            return ResponseEntity.ok(new ApiResponse(true, "Session deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}
