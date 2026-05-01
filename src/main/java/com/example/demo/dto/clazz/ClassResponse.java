package com.example.demo.dto.clazz;

import java.time.LocalDate;

public class ClassResponse {
    private Integer classId;
    private CourseInfo course;
    private TeacherInfo teacher;
    private LocalDate startDate;
    private LocalDate endDate;
    private String schedule;
    private String room;
    private String status;

    public ClassResponse() {
    }

    // Nested class for course info
    public static class CourseInfo {
        private Integer courseId;
        private String courseName;
        private Double price;

        public CourseInfo() {
        }

        public CourseInfo(Integer courseId, String courseName, Double price) {
            this.courseId = courseId;
            this.courseName = courseName;
            this.price = price;
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

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }
    }

    // Nested class for teacher info
    public static class TeacherInfo {
        private Integer userId;
        private String fullName;

        public TeacherInfo() {
        }

        public TeacherInfo(Integer userId, String fullName) {
            this.userId = userId;
            this.fullName = fullName;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }
    }

    // Main ClassResponse constructor
    public ClassResponse(Integer classId, CourseInfo course, TeacherInfo teacher,
            LocalDate startDate, LocalDate endDate, String schedule, String room, String status) {
        this.classId = classId;
        this.course = course;
        this.teacher = teacher;
        this.startDate = startDate;
        this.endDate = endDate;
        this.schedule = schedule;
        this.room = room;
        this.status = status;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public CourseInfo getCourse() {
        return course;
    }

    public void setCourse(CourseInfo course) {
        this.course = course;
    }

    public TeacherInfo getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherInfo teacher) {
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
