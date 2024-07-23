package com.befriend.detour.domain.marker.entity;

import com.befriend.detour.domain.dailyplan.entity.DailyPlan;
import com.befriend.detour.domain.marker.dto.MarkerContentRequestDto;
import com.befriend.detour.domain.place.entity.Place;
import com.befriend.detour.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "markers")
public class Marker extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column
    private String content;

    @Column
    private String images;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MarkerStatusEnum status = MarkerStatusEnum.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_plan_id", nullable = false)
    private DailyPlan dailyPlan;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    public Marker(Double latitude, Double longitude, DailyPlan dailyPlan, Place place) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.dailyPlan = dailyPlan;
        this.place = place;
        this.status = MarkerStatusEnum.ACTIVE;
    }

    public void updateContent(MarkerContentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }

    public void delete() {
        this.status = MarkerStatusEnum.DELETED;
    }

}
