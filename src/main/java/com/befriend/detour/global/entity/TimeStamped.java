package com.befriend.detour.global.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class TimeStamped {

    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modifiedAt")
    private LocalDateTime modifiedAt;

    private LocalDateTime nowInKorea() {
        ZoneId koreaZoneId = ZoneId.of("Asia/Seoul");
        ZonedDateTime nowInKorea = ZonedDateTime.now(koreaZoneId);
        return nowInKorea.toLocalDateTime();
    }

    @PrePersist
    protected void onCreated() { LocalDateTime now = nowInKorea(); this.createdAt = now; this.modifiedAt = now;}
    @PreUpdate
    protected void onUpdate() { this.modifiedAt = nowInKorea(); }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

}
