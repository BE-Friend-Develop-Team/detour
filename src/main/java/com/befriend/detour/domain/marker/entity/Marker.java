package com.befriend.detour.domain.marker.entity;

import com.befriend.detour.domain.dailyplan.entity.DailyPlan;
import com.befriend.detour.domain.file.entity.File;
import com.befriend.detour.domain.marker.dto.MarkerContentRequestDto;
import com.befriend.detour.domain.place.entity.Place;
import com.befriend.detour.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "markers")
public class Marker extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double latitude = 0.0;

    @Column(nullable = false)
    private Double longitude = 0.0;

    @Column
    private String content;

    @Column(nullable = false)
    private Long markerIndex;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MarkerStatusEnum status = MarkerStatusEnum.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_plan_id", nullable = false)
    private DailyPlan dailyPlan;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @OneToMany(mappedBy = "marker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> files = new ArrayList<>();

    public Marker(Double latitude, Double longitude, DailyPlan dailyPlan, Place place, Long markerIndex) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.dailyPlan = dailyPlan;
        this.place = place;
        this.status = MarkerStatusEnum.ACTIVE;
        this.markerIndex = markerIndex;
    }

    public void updateContent(MarkerContentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }

    public void delete() {
        this.status = MarkerStatusEnum.DELETED;
    }

    public void updateIndex(long index) {
        this.markerIndex = index;
    }

}