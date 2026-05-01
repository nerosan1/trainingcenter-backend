package com.example.demo.dto.notification;

import java.time.Instant;

public class NotificationResponse {
    private Integer notificationId;
    private Integer userId;
    private String content;
    private Boolean isRead;
    private Instant createdAt;

    public NotificationResponse() {
    }

    public NotificationResponse(Integer notificationId, Integer userId, String content, Boolean isRead,
            Instant createdAt) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.content = content;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
