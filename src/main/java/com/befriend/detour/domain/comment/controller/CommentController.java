package com.befriend.detour.domain.comment.controller;

import com.befriend.detour.domain.comment.dto.CommentRequestDto;
import com.befriend.detour.domain.comment.dto.CommentResponseDto;
import com.befriend.detour.domain.comment.service.CommentService;
import com.befriend.detour.global.dto.CommonResponseDto;
import com.befriend.detour.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{scheduleId}/comments")
    public ResponseEntity<CommonResponseDto> createComment(@PathVariable Long scheduleId,
                                                           @Valid @RequestBody CommentRequestDto commentRequestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResponseDto commentResponseDto = commentService.createComment(commentRequestDto, scheduleId, userDetails.getUser());

        return new ResponseEntity<>(new CommonResponseDto<>(HttpStatus.CREATED.value(), "댓글 생성에 성공하였습니다. 🎉", commentResponseDto), HttpStatus.CREATED);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommonResponseDto> updateComment(@PathVariable Long commentId,
                                                           @Valid @RequestBody CommentRequestDto commentRequestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResponseDto commentResponseDto = commentService.updateComment(commentRequestDto, commentId, userDetails.getUser());

        return new ResponseEntity<>(new CommonResponseDto<>(HttpStatus.OK.value(), "댓글 수정에 성공하였습니다. 🎉", commentResponseDto), HttpStatus.OK);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<CommonResponseDto> deleteComment(@PathVariable Long commentId,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(commentId, userDetails.getUser());

        return new ResponseEntity<>(new CommonResponseDto<>(HttpStatus.OK.value(), "댓글 삭제에 성공하였습니다. 🎉", null), HttpStatus.OK);
    }

    @GetMapping("/{scheduleId}/comments")
    public ResponseEntity<CommonResponseDto<List<CommentResponseDto>>> getScheduleComments(@PathVariable Long scheduleId,
                                                                                           //@RequestParam(value = "page") int page,
                                                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        Pageable pageable = PageRequest.of(page - 1, 5, Sort.by(Sort.Direction.DESC, "createdAt"));
//        List<CommentResponseDto> commentResponseDtos = commentService.getScheduleComments(scheduleId, userDetails.getUser(), pageable);
        List<CommentResponseDto> commentResponseDtos = commentService.getScheduleComments(scheduleId, userDetails.getUser());

        return new ResponseEntity<>(new CommonResponseDto<>(HttpStatus.OK.value(), "댓글 조회에 성공하였습니다. 🎉", commentResponseDtos), HttpStatus.OK);
    }

}