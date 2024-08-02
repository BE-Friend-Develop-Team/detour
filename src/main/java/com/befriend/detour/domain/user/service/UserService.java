package com.befriend.detour.domain.user.service;

import com.befriend.detour.domain.user.dto.EditPasswordRequestDto;
import com.befriend.detour.domain.user.dto.ProfileResponseDto;
import com.befriend.detour.domain.user.dto.SignupRequestDto;
import com.befriend.detour.domain.user.entity.User;
import com.befriend.detour.domain.user.entity.UserRoleEnum;
import com.befriend.detour.domain.user.entity.UserStatusEnum;
import com.befriend.detour.domain.user.repository.UserRepository;
import com.befriend.detour.global.exception.CustomException;
import com.befriend.detour.global.exception.ErrorCode;
import com.befriend.detour.global.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${ADMIN_TOKEN}")
    private String ADMIN_TOKEN;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

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

        return new ProfileResponseDto(user.getId(), user.getLoginId(), user.getKakaoId(), user.getEmail(), user.getNickname(), user.getStatus(), user.getRole());
    }

    @Transactional
    public ProfileResponseDto updateNickname(User user, String nickname) {
        // 닉네임 중복 검사
        if (isNicknameExist(nickname)) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        user.updateNickname(nickname);
        userRepository.save(user);

        return getProfile(user);
    }

    @Transactional
    public ProfileResponseDto updateEmail(User user, String email) {
        // 이메일 중복 검사
        if (isEmailExist(email)) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        user.updateEmail(email);
        userRepository.save(user);

        return getProfile(user);
    }

    @Transactional
    public void updatePassword(User user, EditPasswordRequestDto editPasswordRequestDtoDto) {
        // 현재 비밀번호 체크
        if(!passwordEncoder.matches(editPasswordRequestDtoDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }

        // 새로운 비밀번호 체크
        if(!editPasswordRequestDtoDto.getNewPassword().equals(editPasswordRequestDtoDto.getConfirmNewPassword())) {
            throw new CustomException(ErrorCode.CONFIRM_NEW_PASSWORD_NOT_MATCH);
        }

        String encodePassword = passwordEncoder.encode(editPasswordRequestDtoDto.getNewPassword());

        user.updatePassword(encodePassword);
        userRepository.save(user);
    }

    @Transactional
    public void withdrawalUser(User user, String password) {
        // 비밀번호 확인
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }

        user.updateStatus(UserStatusEnum.WITHDRAWAL);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean isLoginIdExist(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    @Transactional(readOnly = true)
    public boolean isNicknameExist(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Transactional(readOnly = true)
    public boolean isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findUserByNickName(String nickname) {
        return userRepository.findByNickname(nickname).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public void refreshAccessToken(String nickname, HttpServletResponse response) {
        User user = userRepository.findByNickname(nickname).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 리프레시 토큰 검증
        if (!jwtProvider.validateRefreshToken(user.getRefreshToken())) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_VALIDATE);
        }

        // 통과했으면 AccessToken 생성
        String accessToken = jwtProvider.createAccessToken(user.getNickname(), user.getRole());

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public boolean isSameUser(User user1, User user2) {

        return user1.getNickname().equals(user2.getNickname());
    }

}