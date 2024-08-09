package com.befriend.detour.domain.comment.service;

import com.befriend.detour.domain.comment.dto.CommentRequestDto;
import com.befriend.detour.domain.comment.dto.CommentResponseDto;
import com.befriend.detour.domain.comment.entity.Comment;
import com.befriend.detour.domain.comment.repository.CommentRepository;
import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.schedule.service.ScheduleService;
import com.befriend.detour.domain.user.entity.User;
import com.befriend.detour.global.exception.CustomException;
import com.befriend.detour.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleService scheduleService;

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, Long scheduleId, User user) {
        Schedule schedule = scheduleService.findById(scheduleId);
        Comment comment = new Comment(commentRequestDto, schedule, user);
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(CommentRequestDto commentRequestDto, Long commentId, User user) {
        Comment comment = findById(commentId);
        validateUserMatch(comment, user);
        comment.update(commentRequestDto);
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, User user) {
        Comment comment = findById(commentId);
        validateUserMatch(comment, user);
        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getScheduleComments(Long scheduleId, User user) {

        return commentRepository.getPagedCommentsByScheduleAndUser(scheduleId, user.getId());
    }

    @Transactional(readOnly = true)
    public Comment findById(Long commentId) {

        return commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(ErrorCode.COMMENT_NOT_FOUND)
        );
    }

    private void validateUserMatch(Comment comment, User user) {
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.USER_NOT_MATCH_WITH_COMMENT);
        }
    }

}