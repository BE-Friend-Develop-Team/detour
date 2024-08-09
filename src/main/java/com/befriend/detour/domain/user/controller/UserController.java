package com.befriend.detour.domain.user.controller;

import com.befriend.detour.domain.user.dto.*;
import com.befriend.detour.domain.user.service.EmailCertificationService;
import com.befriend.detour.domain.user.service.KakaoService;
import com.befriend.detour.domain.user.service.UserService;
import com.befriend.detour.global.dto.CommonResponseDto;
import com.befriend.detour.global.security.UserDetailsImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.handler.codec.MessageAggregationException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;
    private final EmailCertificationService emailCertificationService;

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
    public ResponseEntity<CommonResponseDto> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException, UnsupportedEncodingException {
        List<String> kakaoToken = kakaoService.kakaoLogin(code, response);

        return ResponseEntity.ok(new CommonResponseDto(200, "카카오 로그인 성공", kakaoToken));
    }

    @GetMapping("/profile")
    public ResponseEntity<CommonResponseDto> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProfileResponseDto profileResponseDto = userService.getProfile(userDetails.getUser());

        return ResponseEntity.ok(new CommonResponseDto(200, "프로필 조회에 성공하였습니다. 🎉", profileResponseDto));
    }

    @PatchMapping("/profiles/nickname")
    public ResponseEntity<CommonResponseDto> updateNickname(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @Valid @RequestBody EditNicknameRequestDto editNicknameRequestDto) {
        ProfileResponseDto profileResponseDto = userService.updateNickname(userDetails.getUser(), editNicknameRequestDto.getNickname());

        return ResponseEntity.ok(new CommonResponseDto(200, "닉네임 수정에 성공하였습니다. 🎉", profileResponseDto));
    }

    @PatchMapping("/profiles/email")
    public ResponseEntity<CommonResponseDto> updateEmail(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @Valid @RequestBody EditEmailRequestDto editEmailRequestDto) {
        ProfileResponseDto profileResponseDto = userService.updateEmail(userDetails.getUser(), editEmailRequestDto.getEmail());

        return ResponseEntity.ok(new CommonResponseDto(200, "이메일 수정에 성공하였습니다. 🎉", profileResponseDto));
    }

    @PatchMapping("/profiles/password")
    public ResponseEntity<CommonResponseDto> updatePassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @Valid @RequestBody EditPasswordRequestDto editPasswordRequestDto) {
        userService.updatePassword(userDetails.getUser(), editPasswordRequestDto);

        return ResponseEntity.ok(new CommonResponseDto(200, "비밀번호 수정에 성공하였습니다. 🎉", null));
    }

    @PatchMapping("/withdrawal")
    public ResponseEntity<CommonResponseDto> withdrawalUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @RequestBody WithdrawalDto withDrawalDto) {
        userService.withdrawalUser(userDetails.getUser(), withDrawalDto.getPassword());

        return ResponseEntity.ok(new CommonResponseDto(200, "회원 탈퇴에 성공하였습니다. 🎉", null));
    }

    @PostMapping("/token")
    public ResponseEntity<CommonResponseDto> refreshAccessToken(@RequestBody RefreshAccessTokenRequestDto refreshAccessTokenRequestDto,
                                                                HttpServletResponse response) {
        userService.refreshAccessToken(refreshAccessTokenRequestDto.getNickname(), response);

        return ResponseEntity.ok(new CommonResponseDto(200, "액세스 토큰 재발급에 성공하였습니다. 🎉", null));
    }

    @PostMapping("/send-certification")
    public ResponseEntity<CommonResponseDto> sendCertificationNumber(@Validated @RequestBody UserCertificateAccountRequestDto request) throws NoSuchAlgorithmException, MessageAggregationException, MessagingException {
        emailCertificationService.sendEmailForCertification(request.getEmail());

        return ResponseEntity.ok(new CommonResponseDto(200, "인증 이메일 전송에 성공하였습니다. 🎉", null));
    }

    @GetMapping("/verify")
    public ResponseEntity<CommonResponseDto> verifyCertificationNumber(@RequestParam(name = "certificationNumber") String certificationNumber,
                                                                       @RequestParam(name = "email") String email) {
        emailCertificationService.verifyEmail(certificationNumber, email);

        return ResponseEntity.ok(new CommonResponseDto(200, "이메일 인증에 성공하였습니다. 🎉", null));
    }

    @GetMapping("/check-loginId")
    public ResponseEntity<CommonResponseDto> checkLoginId(@RequestParam(name = "loginId") String loginId) {
        boolean isExist = userService.isLoginIdExist(loginId);

        return ResponseEntity.ok(new CommonResponseDto(200, "아이디 중복 확인에 성공하였습니다. 🎉", isExist));
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<CommonResponseDto> checkNickname(@RequestParam(name = "nickname") String nickname) {
        boolean isExist = userService.isNicknameExist(nickname);

        return ResponseEntity.ok(new CommonResponseDto(200, "닉네임 중복 확인에 성공하였습니다. 🎉", isExist));
    }

    @GetMapping("/check-email")
    public ResponseEntity<CommonResponseDto> checkEmail(@RequestParam(name = "email") String email) {
        boolean isExist = userService.isEmailExist(email);

        return ResponseEntity.ok(new CommonResponseDto(200, "이메일 중복 확인에 성공하였습니다. 🎉", isExist));
    }

}