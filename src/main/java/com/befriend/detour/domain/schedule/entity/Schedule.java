package com.befriend.detour.domain.schedule.entity;

import com.befriend.detour.domain.dailyplan.entity.DailyPlan;
import com.befriend.detour.domain.invitation.entity.Invitation;
import com.befriend.detour.domain.like.entity.Like;
import com.befriend.detour.domain.schedule.dto.EditDateRequestDto;
import com.befriend.detour.domain.schedule.dto.EditMainImageRequestDto;
import com.befriend.detour.domain.schedule.dto.EditTitleRequestDto;
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

    public Schedule(ScheduleRequestDto scheduleRequestDto, User user, String defaultImageUrl) {
        this.title = scheduleRequestDto.getTitle();
        this.imageUrl = defaultImageUrl;
        this.departureDate = scheduleRequestDto.getDepartureDate();
        this.arrivalDate = scheduleRequestDto.getArrivalDate();
        this.user = user;
    }

    public void updateScheduleTitle(EditTitleRequestDto editTitleRequestDto) {
        this.title = editTitleRequestDto.getTitle();
    }

    public void updateScheduleDate(EditDateRequestDto editDateRequestDto) {
        this.departureDate = editDateRequestDto.getDepartureDate();
        this.arrivalDate = editDateRequestDto.getArrivalDate();
    }

    public void updateScheduleMainImage(EditMainImageRequestDto editMainImageRequestDto) {
        this.imageUrl = editMainImageRequestDto.getMainImage();
    }

}
