package com.befriend.detour.domain.user.controller;

import com.befriend.detour.domain.user.dto.*;
import com.befriend.detour.domain.user.service.KakaoService;
import com.befriend.detour.domain.user.service.UserService;
import com.befriend.detour.global.dto.CommonResponseDto;
import com.befriend.detour.global.security.UserDetailsImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);

        return new ResponseEntity<>(new CommonResponseDto(201, "회원가입에 성공하였습니다. 🌠", null), HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    public ResponseEntity<CommonResponseDto> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.logout(userDetails.getUser());

        return ResponseEntity.ok(new CommonResponseDto(200, "로그아웃에 성공하였습니다. 🎉", null));
    }

    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<CommonResponseDto> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        kakaoService.kakaoLogin(code, response);

        return ResponseEntity.ok(new CommonResponseDto(200, "카카오톡 로그인에 성공하였습니다. 🌠", null));
    }

    @GetMapping("/profile")
    public ResponseEntity<CommonResponseDto> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProfileResponseDto profileResponseDto = userService.getProfile(userDetails.getUser());

        return ResponseEntity.ok(new CommonResponseDto(200, "프로필 조회에 성공하였습니다. 🎉", profileResponseDto));
    }

    @PatchMapping("/profiles/nickname")
    public ResponseEntity<CommonResponseDto> updateNickname(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody EditNicknameRequestDto editNicknameRequestDto) {
        ProfileResponseDto profileResponseDto = userService.updateNickname(userDetails.getUser(), editNicknameRequestDto.getNickname());

        return ResponseEntity.ok(new CommonResponseDto(200, "닉네임 수정에 성공하였습니다. 🎉", profileResponseDto));
    }

    @PatchMapping("/profiles/email")
    public ResponseEntity<CommonResponseDto> updateEmail(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody EditEmailRequestDto editEmailRequestDto) {
        ProfileResponseDto profileResponseDto = userService.updateEmail(userDetails.getUser(), editEmailRequestDto.getEmail());

        return ResponseEntity.ok(new CommonResponseDto(200, "이메일 수정에 성공하였습니다. 🎉", profileResponseDto));
    }

    @PatchMapping("/profiles/password")
    public ResponseEntity<CommonResponseDto> updatePassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody EditPasswordDto editPasswordDto) {
        userService.updatePassword(userDetails.getUser(), editPasswordDto);

        return ResponseEntity.ok(new CommonResponseDto(200, "비밀번호 수정에 성공하였습니다. 🎉", null));
    }


    @PatchMapping("/withdrawal")
    public ResponseEntity<CommonResponseDto> withdrawalUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.withdrawalUser(userDetails.getUser());

        return ResponseEntity.ok(new CommonResponseDto(200, "회원 탈퇴에 성공하였습니다. 🎉", null));
    }

}
