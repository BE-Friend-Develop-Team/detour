package com.befriend.detour.domain.user.entity;

import com.befriend.detour.domain.user.dto.SignupRequestDto;
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
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column
    private String loginId;

    @Column
    private Long kakaoId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatusEnum status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRoleEnum role;

    public User(SignupRequestDto signupRequestDto, UserStatusEnum userStatus, UserRoleEnum userRole) {
        this.email = signupRequestDto.getEmail();
        this.loginId = signupRequestDto.getLoginId();
        this.nickname = signupRequestDto.getNickname();
        this.status = userStatus;
        this.role = userRole;
    }

    public User(String email, String encodedPassword, String nickname, UserStatusEnum userStatusEnum, UserRoleEnum userRoleEnum, Long kakaoId) {
        this.password = encodedPassword;
        this.nickname = nickname;
        this.email = email;
        this.status = userStatusEnum;
        this.role = userRoleEnum;
        this.kakaoId = kakaoId;
    }

    public void encryptionPassword(String password) {
        this.password = password;
    }

    public void updateRefresh(String refresh) {
        this.refreshToken = refresh;
    }

    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;

        return this;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateStatus(UserStatusEnum status) {
        this.status = status;
    }

}