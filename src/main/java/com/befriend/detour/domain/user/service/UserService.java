package com.befriend.detour.domain.user.service;

import com.befriend.detour.domain.user.dto.SignupRequestDto;
import com.befriend.detour.domain.user.entity.User;
import com.befriend.detour.domain.user.entity.UserRoleEnum;
import com.befriend.detour.domain.user.entity.UserStatusEnum;
import com.befriend.detour.domain.user.repository.UserRepository;
import com.befriend.detour.global.exception.CustomException;
import com.befriend.detour.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${ADMIN_TOKEN}")
    private String ADMIN_TOKEN;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {
        // 아이디 중복 검사
        if (isLoginIdExist(signupRequestDto.getLoginId())) {
            throw new CustomException(ErrorCode.DUPLICATE_USER_ID);
        }

        // 닉네임 중복 검사
        if (isNicknameExist(signupRequestDto.getNickname())) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        // 이메일 중복 검사
        if (isEmailExist(signupRequestDto.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        UserRoleEnum userRole = UserRoleEnum.USER;
        if (!signupRequestDto.getAdminToken().isEmpty()) {
            if (!ADMIN_TOKEN.equals(signupRequestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            userRole = userRole.MANAGER;
        }

        UserStatusEnum userStatus = UserStatusEnum.ACTIVE;
        User user = new User(signupRequestDto, userStatus, userRole);
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

        user.encryptionPassword(encodedPassword);
        userRepository.save(user);
    }

    public boolean isLoginIdExist(String loginId) {
        Optional<User> user = userRepository.findByLoginId(loginId);

        return user.isPresent();
    }

    public boolean isNicknameExist(String nickname) {
        Optional<User> user = userRepository.findByNickname(nickname);

        return user.isPresent();
    }

    public boolean isEmailExist(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        return user.isPresent();
    }

    public User findUserByNickName(String nickname) {
        return userRepository.findByNickname(nickname).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

}
