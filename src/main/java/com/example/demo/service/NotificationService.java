package com.example.demo.service;

import com.example.demo.dto.notification.NotificationResponse;
import com.example.demo.entity.Notification;
import com.example.demo.entity.User;
import com.example.demo.repository.AuditLogRepository;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository,
            AuditLogRepository auditLogRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public NotificationResponse sendNotification(Integer userId, String content, Integer actorId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setContent(content);
        notification.setIsRead(false);

        Notification saved = notificationRepository.save(notification);
        logAction(actorId, "SEND_NOTIFICATION", "Notification", saved.getNotificationId());

        return toResponse(saved);
    }

    @Transactional
    public NotificationResponse broadcastNotification(List<Integer> userIds, String content, Integer actorId) {
        for (Integer userId : userIds) {
            sendNotification(userId, content, actorId);
        }
        return null;
    }

    public List<NotificationResponse> getNotificationsByUser(Integer userId) {
        return notificationRepository.findByUserUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<NotificationResponse> getUnreadNotifications(Integer userId) {
        return notificationRepository.findByUserUserId(userId).stream()
                .filter(n -> !n.getIsRead())
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public NotificationResponse markAsRead(Integer notificationId, Integer actorId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setIsRead(true);
        Notification saved = notificationRepository.save(notification);
        logAction(actorId, "MARK_NOTIFICATION_READ", "Notification", saved.getNotificationId());

        return toResponse(saved);
    }

    @Transactional
    public void markAllAsRead(Integer userId, Integer actorId) {
        List<Notification> notifications = notificationRepository.findByUserUserId(userId);
        for (Notification n : notifications) {
            n.setIsRead(true);
            notificationRepository.save(n);
        }
        logAction(actorId, "MARK_ALL_NOTIFICATIONS_READ", "User", userId);
    }

    @Transactional
    public void deleteNotification(Integer id, Integer actorId) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notificationRepository.delete(notification);
        logAction(actorId, "DELETE_NOTIFICATION", "Notification", id);
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

    private NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getNotificationId(),
                notification.getUser().getUserId(),
                notification.getContent(),
                notification.getIsRead(),
                notification.getCreatedAt());
    }
}
