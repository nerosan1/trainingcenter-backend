package com.example.demo.dto.attendance;

import java.time.Instant;

public class AttendanceResponse {
    private Integer attendanceId;
    private Integer sessionId;
    private Integer studentId;
    private String studentName;
    private String status;
    private String note;
    private Instant markedAt;

    public AttendanceResponse() {
    }

    public AttendanceResponse(Integer attendanceId, Integer sessionId, Integer studentId, String studentName,
            String status, String note, Instant markedAt) {
        this.attendanceId = attendanceId;
        this.sessionId = sessionId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.status = status;
        this.note = note;
        this.markedAt = markedAt;
    }

    public Integer getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Integer attendanceId) {
        this.attendanceId = attendanceId;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Instant getMarkedAt() {
        return markedAt;
    }

    public void setMarkedAt(Instant markedAt) {
        this.markedAt = markedAt;
    }
}
