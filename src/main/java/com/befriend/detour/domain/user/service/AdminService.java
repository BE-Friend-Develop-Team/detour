package com.befriend.detour.domain.user.service;

import com.befriend.detour.domain.user.dto.ProfileResponseDto;
import com.befriend.detour.domain.user.entity.User;
import com.befriend.detour.domain.user.repository.UserRepository;
import com.befriend.detour.global.exception.CustomException;
import com.befriend.detour.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public Page<ProfileResponseDto> getAllUsers(Pageable pageable) {
        List<User> users = userRepository.findAllUser(pageable).orElseThrow(() -> new CustomException(ErrorCode.NO_MEMBERS_FOUND));

        // User 객체를 ProfileResponseDto로 변환
        List<ProfileResponseDto> profileResponseDtos = users.stream()
                .map(user -> new ProfileResponseDto(user.getId(), user.getLoginId(), user.getKakaoId(), user.getEmail(), user.getNickname(), user.getStatus(), user.getRole()))
                .collect(Collectors.toList());

        return new PageImpl<>(profileResponseDtos, pageable, profileResponseDtos.size());
    }

}