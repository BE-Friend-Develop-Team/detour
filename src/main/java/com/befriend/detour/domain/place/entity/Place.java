package com.befriend.detour.domain.place.entity;

import com.befriend.detour.domain.marker.entity.Marker;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "places")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    private String name;
    private String address;
    private String telNumber;

    @OneToOne(mappedBy = "marker", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Marker marker;

}
