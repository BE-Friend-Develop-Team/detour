package com.befriend.detour.domain.comment.repository;

import com.befriend.detour.domain.comment.dto.CommentResponseDto;
import com.befriend.detour.domain.comment.entity.Comment;
import com.befriend.detour.domain.comment.entity.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CommentResponseDto> getPagedCommentsByScheduleAndUser(Long scheduleId, Long userId) {
        QComment comment = QComment.comment;

        List<Comment> comments = jpaQueryFactory.selectFrom(comment)
                .where(comment.schedule.id.eq(scheduleId))
                .orderBy(comment.createdAt.desc())
                .fetch();

        return comments.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

}