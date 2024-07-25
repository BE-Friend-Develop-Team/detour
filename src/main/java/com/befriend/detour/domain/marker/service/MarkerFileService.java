package com.befriend.detour.domain.marker.service;

import com.befriend.detour.domain.file.entity.File;
import com.befriend.detour.domain.file.service.FileService;
import com.befriend.detour.domain.marker.dto.MarkerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarkerFileService {

    private final FileService fileService;
    private final MarkerService markerService;

    @Transactional
    public List<MarkerResponseDto> uploadFiles(List<MultipartFile> multipartFiles) {
        List<File> fileEntities = fileService.uploadFile(multipartFiles);

        // 파일 정보를 포함한 MarkerResponseDto 생성
        return fileEntities.stream()
                .map(file -> new MarkerResponseDto(null, null, null, List.of(file.getFileUrl())))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFile(Long markerId, String fileUrl) {
        markerService.findMarker(markerId); // 마커 존재 확인
        fileService.deleteFile(fileUrl); // 파일 삭제
    }

}