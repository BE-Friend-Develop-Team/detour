package com.befriend.detour.domain.comment.repository;

import com.befriend.detour.domain.comment.dto.CommentResponseDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepositoryCustom {

    List<CommentResponseDto> getPagedCommentsByScheduleAndUser(Long scheduleId, Long userId);

}