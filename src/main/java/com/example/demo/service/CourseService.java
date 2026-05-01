package com.example.demo.service;

import com.example.demo.dto.course.CreateCourseRequest;
import com.example.demo.dto.course.CourseResponse;
import com.example.demo.entity.Course;
import com.example.demo.entity.CourseStatus;
import com.example.demo.repository.AuditLogRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository,
            AuditLogRepository auditLogRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public CourseResponse createCourse(CreateCourseRequest request, Integer actorId) {
        Course course = new Course();
        course.setCourseName(request.getCourseName());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setDuration(request.getDuration());
        course.setStatus(request.getStatus() != null ? CourseStatus.valueOf(request.getStatus().toUpperCase())
                : CourseStatus.OPEN);

        Course saved = courseRepository.save(course);
        logAction(actorId, "CREATE_COURSE", "Course", saved.getCourseId());

        return toResponse(saved);
    }

    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public CourseResponse getCourseById(Integer id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
        return toResponse(course);
    }

    @Transactional
    public CourseResponse updateCourse(Integer id, CreateCourseRequest request, Integer actorId) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));

        course.setCourseName(request.getCourseName());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setDuration(request.getDuration());
        if (request.getStatus() != null) {
            course.setStatus(CourseStatus.valueOf(request.getStatus().toUpperCase()));
        }

        Course saved = courseRepository.save(course);
        logAction(actorId, "UPDATE_COURSE", "Course", saved.getCourseId());

        return toResponse(saved);
    }

    @Transactional
    public void deleteCourse(Integer id, Integer actorId) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
        course.setStatus(CourseStatus.CLOSED);
        courseRepository.save(course);
        logAction(actorId, "DELETE_COURSE", "Course", id);
    }

    public List<CourseResponse> getOpenCourses() {
        return courseRepository.findAll().stream()
                .filter(c -> c.getStatus() == CourseStatus.OPEN)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private void logAction(Integer userId, String action, String entity, Integer entityId) {
        if (userId != null) {
            try {
                var actor = userRepository.findById(userId).orElse(null);
                if (actor != null) {
                    var log = new com.example.demo.entity.AuditLog();
                    log.setUser(actor);
                    log.setAction(action);
                    log.setEntity(entity);
                    log.setEntityId(entityId);
                    auditLogRepository.save(log);
                }
            } catch (Exception ignored) {
            }
        }
    }

    private CourseResponse toResponse(Course course) {
        return new CourseResponse(
                course.getCourseId(),
                course.getCourseName(),
                course.getDescription(),
                course.getPrice(),
                course.getDuration(),
                course.getStatus().name(),
                course.getCreatedAt());
    }
}
