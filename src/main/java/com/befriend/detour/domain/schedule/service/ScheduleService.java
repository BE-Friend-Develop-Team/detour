package com.befriend.detour.domain.schedule.service;

import com.befriend.detour.domain.invitation.entity.Invitation;
import com.befriend.detour.domain.invitation.repository.InvitationRepository;
import com.befriend.detour.domain.like.dto.LikeResponseDto;
import com.befriend.detour.domain.like.entity.Like;
import com.befriend.detour.domain.like.repository.LikeRepository;
import com.befriend.detour.domain.schedule.dto.ScheduleRequestDto;
import com.befriend.detour.domain.schedule.dto.ScheduleResponseDto;
import com.befriend.detour.domain.schedule.dto.ScheduleUpdateRequestDto;
import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.schedule.repository.ScheduleRepository;
import com.befriend.detour.domain.user.entity.User;
import com.befriend.detour.global.exception.CustomException;
import com.befriend.detour.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    @Value("${DEFAULT_IMAGE_URL}")
    private String defaultImageUrl;

    private final ScheduleRepository scheduleRepository;
    private final InvitationRepository invitationRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public ScheduleResponseDto createSchedule(ScheduleRequestDto scheduleRequestDto, User user) {
        Schedule schedule = new Schedule(scheduleRequestDto, user, defaultImageUrl);
        scheduleRepository.save(schedule);
        Invitation invitation = new Invitation(user, schedule);
        invitationRepository.save(invitation);

        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    public ScheduleResponseDto updateSchedule(Long scheduleId, ScheduleUpdateRequestDto updateRequestDto, User user) {
        Schedule schedule = findById(scheduleId);
        invitationRepository.checkIfMemberOfSchedule(schedule, user);
        updateScheduleFields(schedule, updateRequestDto);
        scheduleRepository.save(schedule);

        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    public void deleteSchedule(Long scheduleId, User user) {
        Schedule checkSchedule = findById(scheduleId);
        invitationRepository.checkIfMemberOfSchedule(checkSchedule, user);
        scheduleRepository.delete(checkSchedule);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getUserCreatedSchedules(Pageable pageable, Long userId, String search) {
        List<Schedule> schedules = scheduleRepository.findSchedulesByCreatedUser(userId, pageable).orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        if (schedules.isEmpty()) {
            throw new CustomException(ErrorCode.USER_CREATED_SCHEDULES_NOT_FOUND);
        }

        schedules = filteringSearch(search, schedules);

        return schedules.stream()
                .map(schedule -> new ScheduleResponseDto(schedule, getLikeResponseDto(userId, schedule.getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getUserLikedSchedules(Pageable pageable, User user, String search) {
        List<Like> likes = likeRepository.getUserLikedSchedules(user, pageable)
                .orElseThrow(() -> new CustomException(ErrorCode.LIKE_NOT_EXIST));

        List<Schedule> schedules = likes.stream()
                .map(Like::getSchedule)
                .collect(Collectors.toList());

        schedules = filteringSearch(search, schedules);

        return schedules.stream()
                .map(schedule -> new ScheduleResponseDto(schedule, getLikeResponseDto(user.getId(), schedule.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public ScheduleResponseDto getSchedule(Long scheduleId, User user) {
        Schedule schedule = findById(scheduleId);

        scheduleRepository.updateHits(scheduleId);
        scheduleRepository.updateHourHits(scheduleId);

        LikeResponseDto likeResponseDto = getLikeResponseDto(user.getId(), scheduleId);

        return new ScheduleResponseDto(schedule, likeResponseDto);
    }

    @Transactional(readOnly = true)
    public List<Long> getRanking() {
        return scheduleRepository.getScheduleIdRanking()
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getSchedules(String sortBy, int page, int size, String search, User user) {
        Sort sort;

        if (sortBy.equals("좋아요")) {
            sort = Sort.by(Sort.Order.desc("likeCount"));
        } else if (sortBy.equals("최신")) {
            sort = Sort.by(Sort.Order.desc("createdAt"));
        } else {
            throw new CustomException(ErrorCode.SORT_NOT_FOUND);
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        List<Schedule> schedules = scheduleRepository.findAll(pageable).getContent();

        schedules = filteringSearch(search, schedules);

        return schedules.stream()
                .map(schedule -> new ScheduleResponseDto(schedule, getLikeResponseDto(user.getId(), schedule.getId())))
                .collect(Collectors.toList());
    }

    private void updateScheduleFields(Schedule schedule, ScheduleUpdateRequestDto updateRequestDto) {
        if (updateRequestDto.getTitle() != null) {
            schedule.updateScheduleTitle(updateRequestDto.getTitle());
        }
        if (updateRequestDto.getDepartureDate() != null) {
            schedule.updateDepartureDate(updateRequestDto.getDepartureDate());
        }
        if (updateRequestDto.getArrivalDate() != null) {
            schedule.updateArrivalDate(updateRequestDto.getArrivalDate());
        }
        if (updateRequestDto.getMainImage() != null) {
            schedule.updateScheduleMainImage(updateRequestDto.getMainImage());
        }
    }

    @Transactional(readOnly = true)
    public Schedule findById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND)
        );
    }

    private LikeResponseDto getLikeResponseDto(Long userId, Long scheduleId) {
        boolean isLiked = false;

        Optional<Like> like = likeRepository.findByUserIdAndScheduleId(userId, scheduleId);
        if (like.isPresent()) {
            isLiked = true;
        } else {
            isLiked = false;
        }
        return new LikeResponseDto(null, scheduleId, isLiked);
    }

    public List<Schedule> filteringSearch(String search, List<Schedule> schedules) {
        if (search != null && !search.isEmpty()) {
            schedules = schedules.stream()
                    .filter(schedule -> schedule.getTitle().contains(search))
                    .collect(Collectors.toList());

            if (schedules.isEmpty()) {
                throw new CustomException(ErrorCode.SCHEDULE_NOT_FOUND);
            }
        }

        return schedules;
    }

    public void deleteAllHourHits() {
        scheduleRepository.deleteAllHourHits();
    }
}
