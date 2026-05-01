package com.example.demo.dto.assignment;

import java.time.LocalDate;

public class AssignmentResponse {
    private Integer assignmentId;
    private Integer classId;
    private String className;
    private String title;
    private String description;
    private LocalDate dueDate;

    public AssignmentResponse() {
    }

    public AssignmentResponse(Integer assignmentId, Integer classId, String className, String title, String description,
            LocalDate dueDate) {
        this.assignmentId = assignmentId;
        this.classId = classId;
        this.className = className;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    public Integer getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Integer assignmentId) {
        this.assignmentId = assignmentId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
