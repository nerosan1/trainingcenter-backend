package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.assignment.AssignmentResponse;
import com.example.demo.dto.assignment.CreateAssignmentRequest;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    public ResponseEntity<?> createAssignment(@Valid @RequestBody CreateAssignmentRequest request,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Integer actorId = userDetails != null ? userDetails.getUserId() : null;
            AssignmentResponse response = assignmentService.createAssignment(request, actorId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAssignmentById(@PathVariable Integer id) {
        try {
            AssignmentResponse response = assignmentService.getAssignmentById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<?> getAssignmentsByClass(@PathVariable Integer classId) {
        try {
            List<AssignmentResponse> assignments = assignmentService.getAssignmentsByClass(classId);
            return ResponseEntity.ok(assignments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/class/{classId}/upcoming")
    public ResponseEntity<?> getUpcomingAssignments(@PathVariable Integer classId) {
        try {
            List<AssignmentResponse> assignments = assignmentService.getUpcomingAssignments(classId);
            return ResponseEntity.ok(assignments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAssignment(@PathVariable Integer id,
                                               @RequestBody CreateAssignmentRequest request,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Integer actorId = userDetails != null ? userDetails.getUserId() : null;
            AssignmentResponse response = assignmentService.updateAssignment(id, request, actorId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAssignment(@PathVariable Integer id,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Integer actorId = userDetails != null ? userDetails.getUserId() : null;
            assignmentService.deleteAssignment(id, actorId);
            return ResponseEntity.ok(new ApiResponse(true, "Assignment deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}
