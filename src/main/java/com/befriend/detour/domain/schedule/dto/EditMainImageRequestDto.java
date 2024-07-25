package com.befriend.detour.domain.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EditMainImageRequestDto {

    @NotBlank(message = "이미지를 입력해주세요.")
    String mainImage;

}