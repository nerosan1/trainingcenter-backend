package com.example.demo.dto.enrollment;

import java.time.LocalDate;

public class TrainingClassDetailResponse {
    private Integer classId;
    private String courseName;
    private CourseDetailResponse course;
    private TeacherDetailResponse teacher;
    private LocalDate startDate;
    private LocalDate endDate;
    private String schedule;
    private String room;
    private String status;

    public TrainingClassDetailResponse() {
    }

    public TrainingClassDetailResponse(Integer classId, String courseName, CourseDetailResponse course,
            TeacherDetailResponse teacher, LocalDate startDate, LocalDate endDate, String schedule, String room,
            String status) {
        this.classId = classId;
        this.courseName = courseName;
        this.course = course;
        this.teacher = teacher;
        this.startDate = startDate;
        this.endDate = endDate;
        this.schedule = schedule;
        this.room = room;
        this.status = status;
    }

    // Getters and Setters
    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public CourseDetailResponse getCourse() {
        return course;
    }

    public void setCourse(CourseDetailResponse course) {
        this.course = course;
    }

    public TeacherDetailResponse getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherDetailResponse teacher) {
        this.teacher = teacher;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
