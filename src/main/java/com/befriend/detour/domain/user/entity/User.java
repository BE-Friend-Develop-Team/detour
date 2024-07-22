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

    @Column(nullable = false)
    private String loginId;

    @Column
    private String kakaoId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String refreshToken;

    @Column(nullable = false)
    private UserStatusEnum status;

    @Column(nullable = false)
    private UserRoleEnum role;

    public User(SignupRequestDto signupRequestDto, UserStatusEnum userStatus, UserRoleEnum userRole) {
        this.email = signupRequestDto.getEmail();
        this.loginId = signupRequestDto.getLoginId();
        this.nickname = signupRequestDto.getNickname();
        this.status = userStatus;
        this.role = userRole;
    }

    public void encryptionPassword(String password) {
            this.password = password;
    }

}