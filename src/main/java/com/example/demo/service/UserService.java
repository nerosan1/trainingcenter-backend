package com.example.demo.service;

import com.example.demo.dto.user.CreateUserRequest;
import com.example.demo.dto.user.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.entity.UserStatus;
import com.example.demo.repository.AuditLogRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogRepository auditLogRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AuditLogRepository auditLogRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request, Integer actorId) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.valueOf(request.getRole().toUpperCase()));
        user.setPhone(request.getPhone());
        user.setStatus(UserStatus.ACTIVE);

        User saved = userRepository.save(user);

        logAction(actorId, "CREATE_USER", "User", saved.getUserId());

        return toResponse(saved);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public UserResponse getUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return toResponse(user);
    }

    @Transactional
    public UserResponse updateUser(Integer id, CreateUserRequest request, Integer actorId) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(request.getFullName());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.setPhone(request.getPhone());
        if (request.getRole() != null) {
            user.setRole(UserRole.valueOf(request.getRole().toUpperCase()));
        }

        User saved = userRepository.save(user);
        logAction(actorId, "UPDATE_USER", "User", saved.getUserId());

        return toResponse(saved);
    }

    @Transactional
    public void deleteUser(Integer id, Integer actorId) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
        logAction(actorId, "DELETE_USER", "User", id);
    }

    @Transactional
    public UserResponse assignRole(Integer id, String role, Integer actorId) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(UserRole.valueOf(role.toUpperCase()));
        User saved = userRepository.save(user);
        logAction(actorId, "ASSIGN_ROLE", "User", saved.getUserId());
        return toResponse(saved);
    }

    public List<UserResponse> getUsersByRole(String role) {
        return userRepository.findAll().stream()
                .filter(u -> u.getRole().name().equalsIgnoreCase(role))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    private void logAction(Integer userId, String action, String entity, Integer entityId) {
        if (userId != null) {
            try {
                User actor = userRepository.findById(userId).orElse(null);
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

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                user.getPhone(),
                user.getStatus().name(),
                user.getCreatedAt());
    }
}
