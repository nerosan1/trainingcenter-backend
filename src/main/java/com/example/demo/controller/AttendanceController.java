package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.attendance.AttendanceRecordDto;
import com.example.demo.dto.attendance.AttendanceResponse;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<?> getAttendanceBySession(@PathVariable Integer sessionId) {
        try {
            List<AttendanceResponse> attendances = attendanceService.getAttendanceBySession(sessionId);
            return ResponseEntity.ok(attendances);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getAttendanceByStudent(@PathVariable Integer studentId) {
        try {
            List<AttendanceResponse> attendances = attendanceService.getAttendanceByStudent(studentId);
            return ResponseEntity.ok(attendances);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<?> getAttendanceByClass(@PathVariable Integer classId) {
        try {
            List<AttendanceResponse> attendances = attendanceService.getAttendanceByClass(classId);
            return ResponseEntity.ok(attendances);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/session/{sessionId}")
    public ResponseEntity<?> markAttendance(@PathVariable Integer sessionId,
                                          @RequestBody List<AttendanceRecordDto> records,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Integer actorId = userDetails != null ? userDetails.getUserId() : null;
            List<AttendanceResponse> responses = attendanceService.markAttendance(sessionId, records, actorId);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/session/{sessionId}/student/{studentId}")
    public ResponseEntity<?> markSingleAttendance(@PathVariable Integer sessionId,
                                              @PathVariable Integer studentId,
                                              @RequestBody AttendanceRecordDto record,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Integer actorId = userDetails != null ? userDetails.getUserId() : null;
            AttendanceResponse response = attendanceService.markSingleAttendance(sessionId, studentId, record, actorId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateAttendanceStatus(@PathVariable Integer id,
                                                     @RequestParam String status,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Integer actorId = userDetails != null ? userDetails.getUserId() : null;
            AttendanceResponse response = attendanceService.updateAttendanceStatus(id, status, actorId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/class/{classId}/student/{studentId}/rate")
    public ResponseEntity<?> getAttendanceRate(@PathVariable Integer classId,
                                               @PathVariable Integer studentId) {
        try {
            double rate = attendanceService.getAttendanceRate(studentId, classId);
            return ResponseEntity.ok(new ApiResponse(true, "Attendance rate: " + rate + "%"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}
