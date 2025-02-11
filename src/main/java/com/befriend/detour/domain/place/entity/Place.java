package com.befriend.detour.domain.place.entity;

import com.befriend.detour.domain.marker.entity.Marker;
import com.befriend.detour.domain.place.dto.PlaceRequestDto;
import com.befriend.detour.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "places")
public class Place extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column
    private String telNumber;

    @OneToOne(mappedBy = "place", fetch = FetchType.LAZY)
    private Marker marker;

    public Place(PlaceRequestDto placeRequestDto) {
        this.name = placeRequestDto.getName();
        this.address = placeRequestDto.getAddress();
        this.telNumber = placeRequestDto.getTelNumber();
    }

    public void update(PlaceRequestDto placeRequestDto) {
        if (placeRequestDto.getName() != null) {
            this.name = placeRequestDto.getName();
        }

        if (placeRequestDto.getAddress() != null) {
            this.address = placeRequestDto.getAddress();
        }

        if (placeRequestDto.getTelNumber() != null) {
            this.telNumber = placeRequestDto.getTelNumber();
        }
    }

}
