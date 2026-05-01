package com.example.demo.config;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalTime;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final TrainingClassRepository classRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PaymentRepository paymentRepository;
    private final ClassSessionRepository sessionRepository;
    private final AssignmentRepository assignmentRepository;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, CourseRepository courseRepository,
            TrainingClassRepository classRepository, EnrollmentRepository enrollmentRepository,
            PaymentRepository paymentRepository, ClassSessionRepository sessionRepository,
            AssignmentRepository assignmentRepository, NotificationRepository notificationRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.classRepository = classRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.paymentRepository = paymentRepository;
        this.sessionRepository = sessionRepository;
        this.assignmentRepository = assignmentRepository;
        this.notificationRepository = notificationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        // Create ADMIN
        User admin = new User();
        admin.setFullName("Admin User");
        admin.setEmail("admin@training.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(UserRole.ADMIN);
        admin.setPhone("0912345678");
        admin.setStatus(UserStatus.ACTIVE);
        userRepository.save(admin);

        // Create TEACHERS
        User teacher1 = new User();
        teacher1.setFullName("John Smith");
        teacher1.setEmail("john@training.com");
        teacher1.setPassword(passwordEncoder.encode("teacher123"));
        teacher1.setRole(UserRole.TEACHER);
        teacher1.setPhone("0912345679");
        teacher1.setStatus(UserStatus.ACTIVE);
        userRepository.save(teacher1);

        User teacher2 = new User();
        teacher2.setFullName("Mary Johnson");
        teacher2.setEmail("mary@training.com");
        teacher2.setPassword(passwordEncoder.encode("teacher123"));
        teacher2.setRole(UserRole.TEACHER);
        teacher2.setPhone("0912345680");
        teacher2.setStatus(UserStatus.ACTIVE);
        userRepository.save(teacher2);

        // Create STUDENTS
        User student1 = new User();
        student1.setFullName("Alice Brown");
        student1.setEmail("alice@student.com");
        student1.setPassword(passwordEncoder.encode("student123"));
        student1.setRole(UserRole.STUDENT);
        student1.setPhone("0912345681");
        student1.setStatus(UserStatus.ACTIVE);
        userRepository.save(student1);

        User student2 = new User();
        student2.setFullName("Bob Wilson");
        student2.setEmail("bob@student.com");
        student2.setPassword(passwordEncoder.encode("student123"));
        student2.setRole(UserRole.STUDENT);
        student2.setPhone("0912345682");
        student2.setStatus(UserStatus.ACTIVE);
        userRepository.save(student2);

        User student3 = new User();
        student3.setFullName("Charlie Davis");
        student3.setEmail("charlie@student.com");
        student3.setPassword(passwordEncoder.encode("student123"));
        student3.setRole(UserRole.STUDENT);
        student3.setPhone("0912345683");
        student3.setStatus(UserStatus.ACTIVE);
        userRepository.save(student3);

        User student4 = new User();
        student4.setFullName("Diana Miller");
        student4.setEmail("diana@student.com");
        student4.setPassword(passwordEncoder.encode("student123"));
        student4.setRole(UserRole.STUDENT);
        student4.setPhone("0912345684");
        student4.setStatus(UserStatus.ACTIVE);
        userRepository.save(student4);

        User student5 = new User();
        student5.setFullName("Eve Garcia");
        student5.setEmail("eve@student.com");
        student5.setPassword(passwordEncoder.encode("student123"));
        student5.setRole(UserRole.STUDENT);
        student5.setPhone("0912345685");
        student5.setStatus(UserStatus.ACTIVE);
        userRepository.save(student5);

        User student6 = new User();
        student6.setFullName("Frank Miller");
        student6.setEmail("frank@student.com");
        student6.setPassword(passwordEncoder.encode("student123"));
        student6.setRole(UserRole.STUDENT);
        student6.setPhone("0912345686");
        student6.setStatus(UserStatus.ACTIVE);
        userRepository.save(student6);

        User student7 = new User();
        student7.setFullName("Grace Lee");
        student7.setEmail("grace@student.com");
        student7.setPassword(passwordEncoder.encode("student123"));
        student7.setRole(UserRole.STUDENT);
        student7.setPhone("0912345687");
        student7.setStatus(UserStatus.ACTIVE);
        userRepository.save(student7);

        User student8 = new User();
        student8.setFullName("Henry Clark");
        student8.setEmail("henry@student.com");
        student8.setPassword(passwordEncoder.encode("student123"));
        student8.setRole(UserRole.STUDENT);
        student8.setPhone("0912345688");
        student8.setStatus(UserStatus.ACTIVE);
        userRepository.save(student8);

        User student9 = new User();
        student9.setFullName("Ivy Martinez");
        student9.setEmail("ivy@student.com");
        student9.setPassword(passwordEncoder.encode("student123"));
        student9.setRole(UserRole.STUDENT);
        student9.setPhone("0912345689");
        student9.setStatus(UserStatus.ACTIVE);
        userRepository.save(student9);

        User student10 = new User();
        student10.setFullName("Jack Robinson");
        student10.setEmail("jack@student.com");
        student10.setPassword(passwordEncoder.encode("student123"));
        student10.setRole(UserRole.STUDENT);
        student10.setPhone("0912345690");
        student10.setStatus(UserStatus.ACTIVE);
        userRepository.save(student10);

        // Create COURSES
        Course course1 = new Course();
        course1.setCourseName("Java Programming Fundamentals");
        course1.setDescription("Learn Java from scratch to advanced level");
        course1.setPrice(500.0);
        course1.setDuration(30);
        course1.setStatus(CourseStatus.OPEN);
        courseRepository.save(course1);

        Course course2 = new Course();
        course2.setCourseName("Python for Data Science");
        course2.setDescription("Master Python for data analysis and ML");
        course2.setPrice(600.0);
        course2.setDuration(40);
        course2.setStatus(CourseStatus.OPEN);
        courseRepository.save(course2);

        Course course3 = new Course();
        course3.setCourseName("Web Development with React");
        course3.setDescription("Full-stack web development");
        course3.setPrice(550.0);
        course3.setDuration(35);
        course3.setStatus(CourseStatus.OPEN);
        courseRepository.save(course3);

        Course course4 = new Course();
        course4.setCourseName("AWS Cloud Computing");
        course4.setDescription("Cloud infrastructure on AWS");
        course4.setPrice(700.0);
        course4.setDuration(25);
        course4.setStatus(CourseStatus.OPEN);
        courseRepository.save(course4);

        Course course5 = new Course();
        course5.setCourseName("Mobile App Development");
        course5.setDescription("Build iOS and Android apps");
        course5.setPrice(650.0);
        course5.setDuration(45);
        course5.setStatus(CourseStatus.OPEN);
        courseRepository.save(course5);

        // Create CLASSES
        TrainingClass class1 = new TrainingClass();
        class1.setCourse(course1);
        class1.setTeacher(teacher1);
        class1.setStartDate(LocalDate.now().plusDays(7));
        class1.setEndDate(LocalDate.now().plusDays(37));
        class1.setSchedule("Mon/Wed/Fri 9:00-12:00");
        class1.setRoom("Room 101");
        class1.setStatus(ClassStatus.UPCOMING);
        classRepository.save(class1);

        TrainingClass class2 = new TrainingClass();
        class2.setCourse(course2);
        class2.setTeacher(teacher2);
        class2.setStartDate(LocalDate.now().plusDays(14));
        class2.setEndDate(LocalDate.now().plusDays(54));
        class2.setSchedule("Tue/Thu/Sat 13:00-16:00");
        class2.setRoom("Room 102");
        class2.setStatus(ClassStatus.UPCOMING);
        classRepository.save(class2);

        TrainingClass class3 = new TrainingClass();
        class3.setCourse(course3);
        class3.setTeacher(teacher1);
        class3.setStartDate(LocalDate.now().minusDays(10));
        class3.setEndDate(LocalDate.now().plusDays(25));
        class3.setSchedule("Mon/Wed/Fri 14:00-17:00");
        class3.setRoom("Room 103");
        class3.setStatus(ClassStatus.ONGOING);
        classRepository.save(class3);

        TrainingClass class4 = new TrainingClass();
        class4.setCourse(course4);
        class4.setTeacher(teacher2);
        class4.setStartDate(LocalDate.now().minusDays(20));
        class4.setEndDate(LocalDate.now().plusDays(5));
        class4.setSchedule("Sat/Sun 9:00-15:00");
        class4.setRoom("Room 104");
        class4.setStatus(ClassStatus.ONGOING);
        classRepository.save(class4);

        TrainingClass class5 = new TrainingClass();
        class5.setCourse(course5);
        class5.setTeacher(teacher1);
        class5.setStartDate(LocalDate.now().plusDays(21));
        class5.setEndDate(LocalDate.now().plusDays(66));
        class5.setSchedule("Tue/Thu 18:00-21:00");
        class5.setRoom("Room 105");
        class5.setStatus(ClassStatus.UPCOMING);
        classRepository.save(class5);

        // Create ENROLLMENTS
        Enrollment e1 = new Enrollment();
        e1.setTrainingClass(class3);
        e1.setStudent(student1);
        e1.setStatus(EnrollmentStatus.ACTIVE);
        enrollmentRepository.save(e1);

        Enrollment e2 = new Enrollment();
        e2.setTrainingClass(class3);
        e2.setStudent(student2);
        e2.setStatus(EnrollmentStatus.ACTIVE);
        enrollmentRepository.save(e2);

        Enrollment e3 = new Enrollment();
        e3.setTrainingClass(class3);
        e3.setStudent(student3);
        e3.setStatus(EnrollmentStatus.ACTIVE);
        enrollmentRepository.save(e3);

        Enrollment e4 = new Enrollment();
        e4.setTrainingClass(class4);
        e4.setStudent(student4);
        e4.setStatus(EnrollmentStatus.ACTIVE);
        enrollmentRepository.save(e4);

        Enrollment e5 = new Enrollment();
        e5.setTrainingClass(class4);
        e5.setStudent(student5);
        e5.setStatus(EnrollmentStatus.ACTIVE);
        enrollmentRepository.save(e5);

        // Pending enrollments
        Enrollment e6 = new Enrollment();
        e6.setTrainingClass(class1);
        e6.setStudent(student6);
        e6.setStatus(EnrollmentStatus.PENDING);
        enrollmentRepository.save(e6);

        Enrollment e7 = new Enrollment();
        e7.setTrainingClass(class1);
        e7.setStudent(student7);
        e7.setStatus(EnrollmentStatus.PENDING);
        enrollmentRepository.save(e7);

        // Create PAYMENTS
        Payment p1 = new Payment();
        p1.setEnrollment(e1);
        p1.setAmount(500.0);
        p1.setMethod(PaymentMethod.BANK_TRANSFER);
        p1.setStatus(PaymentStatus.PAID);
        p1.setPaidAt(java.time.Instant.now().minus(java.time.Duration.ofDays(5)));
        paymentRepository.save(p1);

        Payment p2 = new Payment();
        p2.setEnrollment(e2);
        p2.setAmount(500.0);
        p2.setMethod(PaymentMethod.MOMO);
        p2.setStatus(PaymentStatus.PAID);
        p2.setPaidAt(java.time.Instant.now().minus(java.time.Duration.ofDays(4)));
        paymentRepository.save(p2);

        Payment p3 = new Payment();
        p3.setEnrollment(e3);
        p3.setAmount(500.0);
        p3.setMethod(PaymentMethod.CASH);
        p3.setStatus(PaymentStatus.WAITING_CONFIRM);
        paymentRepository.save(p3);

        Payment p4 = new Payment();
        p4.setEnrollment(e4);
        p4.setAmount(700.0);
        p4.setMethod(PaymentMethod.BANK_TRANSFER);
        p4.setStatus(PaymentStatus.PAID);
        p4.setPaidAt(java.time.Instant.now().minus(java.time.Duration.ofDays(3)));
        paymentRepository.save(p4);

        Payment p5 = new Payment();
        p5.setEnrollment(e5);
        p5.setAmount(700.0);
        p5.setMethod(PaymentMethod.CASH);
        p5.setStatus(PaymentStatus.WAITING_CONFIRM);
        paymentRepository.save(p5);

        // Create SESSIONS
        ClassSession s1 = new ClassSession();
        s1.setTrainingClass(class3);
        s1.setSessionDate(LocalDate.now().minusDays(5));
        s1.setStartTime(LocalTime.of(14, 0));
        s1.setEndTime(LocalTime.of(17, 0));
        s1.setTopic("Introduction to React");
        sessionRepository.save(s1);

        ClassSession s2 = new ClassSession();
        s2.setTrainingClass(class3);
        s2.setSessionDate(LocalDate.now().minusDays(3));
        s2.setStartTime(LocalTime.of(14, 0));
        s2.setEndTime(LocalTime.of(17, 0));
        s2.setTopic("React Components and Props");
        sessionRepository.save(s2);

        ClassSession s3 = new ClassSession();
        s3.setTrainingClass(class3);
        s3.setSessionDate(LocalDate.now().minusDays(1));
        s3.setStartTime(LocalTime.of(14, 0));
        s3.setEndTime(LocalTime.of(17, 0));
        s3.setTopic("React State and Hooks");
        sessionRepository.save(s3);

        ClassSession s4 = new ClassSession();
        s4.setTrainingClass(class4);
        s4.setSessionDate(LocalDate.now().minusDays(7));
        s4.setStartTime(LocalTime.of(9, 0));
        s4.setEndTime(LocalTime.of(15, 0));
        s4.setTopic("AWS Fundamentals");
        sessionRepository.save(s4);

        ClassSession s5 = new ClassSession();
        s5.setTrainingClass(class4);
        s5.setSessionDate(LocalDate.now().minusDays(1));
        s5.setStartTime(LocalTime.of(9, 0));
        s5.setEndTime(LocalTime.of(15, 0));
        s5.setTopic("AWS Services Overview");
        sessionRepository.save(s5);

        // Create ASSIGNMENTS
        Assignment a1 = new Assignment();
        a1.setTrainingClass(class3);
        a1.setTitle("React Todo App");
        a1.setDescription("Build a simple todo application using React");
        a1.setDueDate(LocalDate.now().plusDays(7));
        assignmentRepository.save(a1);

        Assignment a2 = new Assignment();
        a2.setTrainingClass(class3);
        a2.setTitle("React Calculator");
        a2.setDescription("Build a calculator using React hooks");
        a2.setDueDate(LocalDate.now().plusDays(14));
        assignmentRepository.save(a2);

        Assignment a3 = new Assignment();
        a3.setTrainingClass(class4);
        a3.setTitle("AWS Infrastructure Setup");
        a3.setDescription("Create VPC and EC2 instances");
        a3.setDueDate(LocalDate.now().plusDays(5));
        assignmentRepository.save(a3);

        // Create NOTIFICATIONS for users
        Notification n1 = new Notification();
        n1.setUser(admin);
        n1.setContent("Welcome to Training Center Management System!");
        n1.setIsRead(false);
        notificationRepository.save(n1);

        Notification n2 = new Notification();
        n2.setUser(teacher1);
        n2.setContent("New class assigned: Java Programming Fundamentals");
        n2.setIsRead(false);
        notificationRepository.save(n2);

        Notification n3 = new Notification();
        n3.setUser(student1);
        n3.setContent("Your enrollment has been approved!");
        n3.setIsRead(false);
        notificationRepository.save(n3);

        Notification n4 = new Notification();
        n4.setUser(student2);
        n4.setContent("New assignment posted: React Todo App");
        n4.setIsRead(false);
        notificationRepository.save(n4);

        Notification n5 = new Notification();
        n5.setUser(student3);
        n5.setContent("Your submission has been graded: 85/100");
        n5.setIsRead(true);
        notificationRepository.save(n5);
    }
}
