package com.example.demo.dto.enrollment;

import java.time.Instant;

public class EnrollmentResponse {
    private Integer enrollmentId;
    private Integer studentId;
    private String studentName;
    private Integer classId;
    private String className;
    private String status;
    private Instant createdAt;
    private TrainingClassDetailResponse trainingClass;

    public EnrollmentResponse() {
    }

    public EnrollmentResponse(Integer enrollmentId, Integer studentId, String studentName, Integer classId,
            String className, String status, Instant createdAt) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.classId = classId;
        this.className = className;
        this.status = status;
        this.createdAt = createdAt;
    }

    public EnrollmentResponse(Integer enrollmentId, Integer studentId, String studentName, Integer classId,
            String className, String status, Instant createdAt, TrainingClassDetailResponse trainingClass) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.classId = classId;
        this.className = className;
        this.status = status;
        this.createdAt = createdAt;
        this.trainingClass = trainingClass;
    }

    public Integer getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Integer enrollmentId) {
        this.enrollmentId = enrollmentId;
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

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public TrainingClassDetailResponse getTrainingClass() {
        return trainingClass;
    }

    public void setTrainingClass(TrainingClassDetailResponse trainingClass) {
        this.trainingClass = trainingClass;
    }
}
