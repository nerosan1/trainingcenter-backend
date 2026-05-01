package com.example.demo.dto.course;

import java.time.Instant;

public class CourseResponse {
    private Integer courseId;
    private String courseName;
    private String description;
    private Double price;
    private Integer duration;
    private String status;
    private Instant createdAt;

    public CourseResponse() {
    }

    public CourseResponse(Integer courseId, String courseName, String description, Double price, Integer duration,
            String status, Instant createdAt) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
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
}
