package com.befriend.detour.domain.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EditTitleRequestDto {

    @NotBlank(message = "일정의 제목을 입력해주세요.")
    private String title;

}