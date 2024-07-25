package com.befriend.detour.domain.user.controller;

import com.befriend.detour.domain.user.dto.ProfileResponseDto;
import com.befriend.detour.domain.user.entity.User;
import com.befriend.detour.domain.user.entity.UserStatusEnum;
import com.befriend.detour.domain.user.service.AdminService;
import com.befriend.detour.global.dto.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<CommonResponseDto> getAllRecipe(@RequestParam("page") int page) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<ProfileResponseDto> profileResponseDtos = adminService.getAllUsers(pageable);

        return ResponseEntity.ok(new CommonResponseDto(200, "전체 사용자 " + page + "번 페이지 조회 성공하였습니다. 🎉", profileResponseDtos));
    }

    @PatchMapping("/users")
    public ResponseEntity<CommonResponseDto> changeUserStatus(@RequestParam("userId") Long userId) {
        User user = adminService.changeUserStatus(userId);
        UserStatusEnum status = user.getStatus();

        return ResponseEntity.ok(new CommonResponseDto(200, userId + "번 유저 상태를 " + status +"로 변경 성공하였습니다. 🎉", null));
    }

}