package com.befriend.detour.domain.review.entity;

import com.befriend.detour.domain.review.dto.ReviewRequestDto;
import com.befriend.detour.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "reviews")
public class Review extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int star; // String에서 int로 변경

    @Column(nullable = false)
    private String username;

    public Review(ReviewRequestDto requestDto) {
        this.content = requestDto.getContent();
        this.star = requestDto.getStar(); // int로 변경
        this.username = requestDto.getUsername();
    }

}