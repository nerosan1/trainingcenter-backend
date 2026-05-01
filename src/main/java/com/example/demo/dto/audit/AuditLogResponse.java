package com.example.demo.dto.audit;

import java.time.Instant;

public class AuditLogResponse {
    private Integer logId;
    private Integer userId;
    private String action;
    private String entity;
    private Integer entityId;
    private Instant createdAt;

    public AuditLogResponse() {
    }

    public AuditLogResponse(Integer logId, Integer userId, String action, String entity, Integer entityId,
            Instant createdAt) {
        this.logId = logId;
        this.userId = userId;
        this.action = action;
        this.entity = entity;
        this.entityId = entityId;
        this.createdAt = createdAt;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
