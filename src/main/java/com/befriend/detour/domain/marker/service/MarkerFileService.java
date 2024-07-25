package com.befriend.detour.domain.marker.service;

import com.befriend.detour.domain.file.entity.File;
import com.befriend.detour.domain.file.repository.FileRepository;
import com.befriend.detour.domain.file.service.FileService;
import com.befriend.detour.domain.marker.dto.MarkerResponseDto;
import com.befriend.detour.domain.marker.entity.Marker;
import com.befriend.detour.global.exception.CustomException;
import com.befriend.detour.global.exception.ErrorCode;
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
    private final FileRepository fileRepository;

    @Transactional
    public List<MarkerResponseDto> uploadFiles(Long markerId, List<MultipartFile> multipartFiles) {
        Marker marker = markerService.findMarker(markerId);
        List<File> fileEntities = fileService.uploadFile(multipartFiles, markerId);

        // 파일 정보를 포함한 MarkerResponseDto 생성
        return fileEntities.stream()
                .map(file -> new MarkerResponseDto(marker.getPlace().getId(), marker.getLatitude(), marker.getLongitude(), marker.getContent(), List.of(file.getFileUrl()), marker.getCreatedAt(), marker.getModifiedAt()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFile(Long markerId, String fileUrl) {
        Marker marker = markerService.findMarker(markerId);
        if (marker == null) {
            throw new CustomException(ErrorCode.MARKER_NOT_FOUND);
        }

        File file = fileService.findFileByUrl(fileUrl);
        if (file == null || !file.getMarker().getId().equals(markerId)) {
            throw new CustomException(ErrorCode.EXTENSION_IS_EMPTY);
        }

        fileService.deleteFile(fileUrl);
        fileRepository.delete(file);
    }

}