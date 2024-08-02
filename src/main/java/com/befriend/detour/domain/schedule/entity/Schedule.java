package com.befriend.detour.domain.schedule.entity;

import com.befriend.detour.domain.dailyplan.entity.DailyPlan;
import com.befriend.detour.domain.invitation.entity.Invitation;
import com.befriend.detour.domain.like.entity.Like;
import com.befriend.detour.domain.schedule.dto.ScheduleRequestDto;
import com.befriend.detour.domain.user.entity.User;
import com.befriend.detour.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "schedules")
public class Schedule extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private LocalDateTime departureDate;

    @Column(nullable = false)
    private LocalDateTime arrivalDate;

    @Column(nullable = false)
    private Long likeCount = 0L;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private Long hits = 0L;

    @Column(name = "hour_hits", columnDefinition = "integer default 0", nullable = false)
    private Long hourHits = 0L;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invitation> invitations;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyPlan> dailyPlans;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void addLikeCount() {
        this.likeCount++;
    }

    public void minusLikeCount() {
        this.likeCount--;
    }

    public Long getHits() {

        return hits;
    }

    public Long getHourHits() {

        return hourHits;
    }

    public Schedule(ScheduleRequestDto scheduleRequestDto, User user, String defaultImageUrl) {
        this.title = scheduleRequestDto.getTitle();
        this.imageUrl = defaultImageUrl;
        this.departureDate = scheduleRequestDto.getDepartureDate();
        this.arrivalDate = scheduleRequestDto.getArrivalDate();
        this.user = user;
    }

    public void updateScheduleTitle(String title) {
        this.title = title;
    }

    public void updateDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public void updateArrivalDate(LocalDateTime arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public void setMainImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
