package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.submission.CreateSubmissionRequest;
import com.example.demo.dto.submission.SubmissionResponse;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.SubmissionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping
    public ResponseEntity<?> createSubmission(@Valid @RequestBody CreateSubmissionRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Integer studentId = userDetails != null ? userDetails.getUserId() : null;
            Integer actorId = userDetails != null ? userDetails.getUserId() : null;
            SubmissionResponse response = submissionService.createSubmission(request, studentId, actorId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSubmissionById(@PathVariable Integer id) {
        try {
            SubmissionResponse response = submissionService.getSubmissionById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<?> getSubmissionsByAssignment(@PathVariable Integer assignmentId) {
        try {
            List<SubmissionResponse> submissions = submissionService.getSubmissionsByAssignment(assignmentId);
            return ResponseEntity.ok(submissions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getSubmissionsByStudent(@PathVariable Integer studentId) {
        try {
            List<SubmissionResponse> submissions = submissionService.getSubmissionsByStudent(studentId);
            return ResponseEntity.ok(submissions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/{id}/grade")
    public ResponseEntity<?> gradeSubmission(@PathVariable Integer id,
            @RequestParam Float grade,
            @RequestParam(required = false) String feedback,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Integer actorId = userDetails != null ? userDetails.getUserId() : null;
            SubmissionResponse response = submissionService.gradeSubmission(id, grade, feedback, actorId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubmission(@PathVariable Integer id,
            @RequestParam String fileUrl,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Integer actorId = userDetails != null ? userDetails.getUserId() : null;
            SubmissionResponse response = submissionService.updateSubmission(id, fileUrl, actorId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubmission(@PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Integer actorId = userDetails != null ? userDetails.getUserId() : null;
            submissionService.deleteSubmission(id, actorId);
            return ResponseEntity.ok(new ApiResponse(true, "Submission deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}
