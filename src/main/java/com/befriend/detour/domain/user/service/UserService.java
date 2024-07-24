package com.befriend.detour.domain.user.service;

import com.befriend.detour.domain.user.dto.EditPasswordDto;
import com.befriend.detour.domain.user.dto.ProfileResponseDto;
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
            userRole = userRole.ADMIN;
        }

        UserStatusEnum userStatus = UserStatusEnum.ACTIVE;
        User user = new User(signupRequestDto, userStatus, userRole);
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

        user.encryptionPassword(encodedPassword);

        userRepository.save(user);
    }

    @Transactional
    public void logout(User user) {
        user.updateRefresh(null);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public ProfileResponseDto getProfile(User user) {

        return new ProfileResponseDto(user.getId(), user.getLoginId(), user.getKakaoId(), user.getEmail(), user.getNickname());
    }

    @Transactional
    public ProfileResponseDto updateNickname(User user, String nickname) {
        user.updateNickname(nickname);
        userRepository.save(user);

        return getProfile(user);
    }

    @Transactional
    public ProfileResponseDto updateEmail(User user, String email) {
        user.updateEmail(email);
        userRepository.save(user);

        return getProfile(user);
    }

    @Transactional
    public void updatePassword(User user, EditPasswordDto editPasswordDto) {
        // 현재 비밀번호 체크
        if(!passwordEncoder.matches(editPasswordDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }

        // 새로운 비밀번호 체크
        if(!editPasswordDto.getNewPassword().equals(editPasswordDto.getConfirmNewPassword())) {
            throw new CustomException(ErrorCode.CONFIRM_NEW_PASSWORD_NOT_MATCH);
        }

        String encodePassword = passwordEncoder.encode(editPasswordDto.getNewPassword());

        user.updatePassword(encodePassword);
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
