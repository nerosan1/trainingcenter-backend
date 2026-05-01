package com.example.demo.service;

import com.example.demo.dto.clazz.ClassResponse;
import com.example.demo.dto.clazz.CreateClassRequest;
import com.example.demo.entity.ClassStatus;
import com.example.demo.entity.TrainingClass;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.repository.AuditLogRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.TrainingClassRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassService {

    private final TrainingClassRepository classRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;

    public ClassService(TrainingClassRepository classRepository, CourseRepository courseRepository,
            UserRepository userRepository, AuditLogRepository auditLogRepository) {
        this.classRepository = classRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public ClassResponse createClass(CreateClassRequest request, Integer actorId) {
        var course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        var teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        if (teacher.getRole() != UserRole.TEACHER) {
            throw new RuntimeException("User is not a TEACHER");
        }

        TrainingClass clazz = new TrainingClass();
        clazz.setCourse(course);
        clazz.setTeacher(teacher);
        clazz.setStartDate(LocalDate.parse(request.getStartDate()));
        clazz.setEndDate(LocalDate.parse(request.getEndDate()));
        clazz.setSchedule(request.getSchedule());
        clazz.setRoom(request.getRoom());
        clazz.setStatus(request.getStatus() != null ? ClassStatus.valueOf(request.getStatus().toUpperCase())
                : ClassStatus.UPCOMING);

        TrainingClass saved = classRepository.save(clazz);
        logAction(actorId, "CREATE_CLASS", "Class", saved.getClassId());

        return toResponse(saved);
    }

    public List<ClassResponse> getAllClasses() {
        return classRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ClassResponse getClassById(Integer id) {
        TrainingClass clazz = classRepository.findById(id).orElseThrow(() -> new RuntimeException("Class not found"));
        return toResponse(clazz);
    }

    public List<ClassResponse> getClassesByCourse(Integer courseId) {
        return classRepository.findAll().stream()
                .filter(c -> c.getCourse().getCourseId().equals(courseId))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ClassResponse> getClassesByTeacher(Integer teacherId) {
        return classRepository.findAll().stream()
                .filter(c -> c.getTeacher().getUserId().equals(teacherId))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ClassResponse updateClass(Integer id, CreateClassRequest request, Integer actorId) {
        TrainingClass clazz = classRepository.findById(id).orElseThrow(() -> new RuntimeException("Class not found"));

        if (request.getCourseId() != null) {
            var course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            clazz.setCourse(course);
        }

        if (request.getTeacherId() != null) {
            var teacher = userRepository.findById(request.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
            if (teacher.getRole() != UserRole.TEACHER) {
                throw new RuntimeException("User is not a TEACHER");
            }
            clazz.setTeacher(teacher);
        }

        if (request.getStartDate() != null) {
            clazz.setStartDate(LocalDate.parse(request.getStartDate()));
        }
        if (request.getEndDate() != null) {
            clazz.setEndDate(LocalDate.parse(request.getEndDate()));
        }

        clazz.setSchedule(request.getSchedule());
        clazz.setRoom(request.getRoom());

        if (request.getStatus() != null) {
            clazz.setStatus(ClassStatus.valueOf(request.getStatus().toUpperCase()));
        }

        TrainingClass saved = classRepository.save(clazz);
        logAction(actorId, "UPDATE_CLASS", "Class", saved.getClassId());

        return toResponse(saved);
    }

    @Transactional
    public ClassResponse updateClassStatus(Integer id, String status, Integer actorId) {
        TrainingClass clazz = classRepository.findById(id).orElseThrow(() -> new RuntimeException("Class not found"));
        clazz.setStatus(ClassStatus.valueOf(status.toUpperCase()));

        TrainingClass saved = classRepository.save(clazz);
        logAction(actorId, "UPDATE_CLASS_STATUS", "Class", saved.getClassId());

        return toResponse(saved);
    }

    @Transactional
    public void deleteClass(Integer id, Integer actorId) {
        TrainingClass clazz = classRepository.findById(id).orElseThrow(() -> new RuntimeException("Class not found"));
        clazz.setStatus(ClassStatus.FINISHED);
        classRepository.save(clazz);
        logAction(actorId, "DELETE_CLASS", "Class", id);
    }

    public TrainingClass getTrainingClassEntity(Integer id) {
        return classRepository.findById(id).orElseThrow(() -> new RuntimeException("Class not found"));
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

    private ClassResponse toResponse(TrainingClass clazz) {
        // Create nested CourseInfo object
        ClassResponse.CourseInfo courseInfo = new ClassResponse.CourseInfo(
                clazz.getCourse().getCourseId(),
                clazz.getCourse().getCourseName(),
                clazz.getCourse().getPrice());

        // Create nested TeacherInfo object
        ClassResponse.TeacherInfo teacherInfo = new ClassResponse.TeacherInfo(
                clazz.getTeacher().getUserId(),
                clazz.getTeacher().getFullName());

        return new ClassResponse(
                clazz.getClassId(),
                courseInfo,
                teacherInfo,
                clazz.getStartDate(),
                clazz.getEndDate(),
                clazz.getSchedule(),
                clazz.getRoom(),
                clazz.getStatus().name());
    }
}
