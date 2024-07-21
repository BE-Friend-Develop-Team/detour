package com.befriend.detour.domain.user.entity;

import com.befriend.detour.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String loginId;
    private String kakaoId;
    private String password;
    private String nickname;
    private String refreshToken;
    private UserStatusEnum status;
    private UserRoleEnum role;

}