package com.befriend.detour.domain.schedule.service;

import com.befriend.detour.domain.invitation.entity.Invitation;
import com.befriend.detour.domain.invitation.repository.InvitationRepository;
import com.befriend.detour.domain.like.dto.LikeResponseDto;
import com.befriend.detour.domain.like.entity.Like;
import com.befriend.detour.domain.like.repository.LikeRepository;
import com.befriend.detour.domain.schedule.dto.ScheduleDetailsResponseDto;
import com.befriend.detour.domain.schedule.dto.ScheduleRequestDto;
import com.befriend.detour.domain.schedule.dto.ScheduleResponseDto;
import com.befriend.detour.domain.schedule.dto.ScheduleUpdateRequestDto;
import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.schedule.repository.ScheduleRepository;
import com.befriend.detour.domain.user.entity.User;
import com.befriend.detour.domain.user.repository.UserRepository;
import com.befriend.detour.global.exception.CustomException;
import com.befriend.detour.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
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
    private final UserRepository userRepository;
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
    public ScheduleDetailsResponseDto updateSchedule(Long scheduleId, ScheduleUpdateRequestDto updateRequestDto, User user) {
        Schedule schedule = findById(scheduleId);
        invitationRepository.checkIfMemberOfSchedule(schedule, user);
        updateScheduleFields(schedule, updateRequestDto);
        scheduleRepository.save(schedule);

        return new ScheduleDetailsResponseDto(schedule);
    }

    @Transactional
    public ScheduleResponseDto updateMainImage(Long scheduleId, String fileUrl, User user) {

        userRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        invitationRepository.checkIfMemberOfSchedule(schedule, user);
        schedule.setMainImage(fileUrl);
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
    public Page<ScheduleResponseDto> getUserCreatedSchedules(Pageable pageable, Long userId, String search) {
        if (search == null || search == "") {
            Page<Schedule> schedules = scheduleRepository.findSchedulesByCreatedUser(userId, pageable);

            List<ScheduleResponseDto> schedulesDto = schedules.stream()
                    .map(ScheduleResponseDto::new)
                    .collect(Collectors.toList());

            return new PageImpl<>(schedulesDto, pageable, schedules.getTotalElements());
        } else {
            Page<Schedule> schedules = scheduleRepository.findSchedulesByCreatedUserBySearch(userId, pageable, search);

            List<ScheduleResponseDto> schedulesDto = schedules.stream()
                    .map(schedule -> new ScheduleResponseDto(schedule, getLikeResponseDto(userId, schedule.getId())))
                    .collect(Collectors.toList());

            return new PageImpl<>(schedulesDto, pageable, schedules.getTotalElements());
        }
    }

    @Transactional(readOnly = true)
    public Page<ScheduleResponseDto> getUserLikedSchedules(Pageable pageable, User user, String search) {
        if (search == null || search == "") {
            Page<Schedule> schedules = likeRepository.getUserLikedSchedules(user, pageable);
            List<ScheduleResponseDto> schedulesDto = schedules.stream()
                    .map(ScheduleResponseDto::new)
                    .collect(Collectors.toList());

            return new PageImpl<>(schedulesDto, pageable, schedules.getTotalElements());
        } else {
            Page<Schedule> schedules = likeRepository.getUserLikedSchedulesBySearch(user, pageable, search);
            List<ScheduleResponseDto> schedulesDto = schedules.stream()
                    .map(schedule -> new ScheduleResponseDto(schedule, getLikeResponseDto(user.getId(), schedule.getId())))
                    .collect(Collectors.toList());

            return new PageImpl<>(schedulesDto, pageable, schedules.getTotalElements());
        }

    }

    @Transactional
    public ScheduleResponseDto getSchedule(Long scheduleId, User user) {
        Schedule schedule = findById(scheduleId);

        scheduleRepository.updateHits(scheduleId);
        scheduleRepository.updateHourHits(scheduleId);

        LikeResponseDto likeResponseDto = getLikeResponseDto(user.getId(), scheduleId);

        return new ScheduleResponseDto(schedule, likeResponseDto);
    }

    @Transactional
    public ScheduleDetailsResponseDto getScheduleDetails(Long scheduleId, User user) {
        Schedule schedule = findById(scheduleId);

        scheduleRepository.updateHits(scheduleId);
        scheduleRepository.updateHourHits(scheduleId);

        LikeResponseDto likeResponseDto = getLikeResponseDto(user.getId(), scheduleId);

        return new ScheduleDetailsResponseDto(schedule, likeResponseDto);
    }

    @Transactional(readOnly = true)
    public List<Long> getRanking() {

        return scheduleRepository.getScheduleIdRanking()
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<ScheduleResponseDto> getSchedules(String sortBy, int page, int size, String search, User user) {
        Sort sort;

        if (sortBy.equals("좋아요")) {
            sort = Sort.by(Sort.Order.desc("likeCount"));
        } else if (sortBy.equals("최신")) {
            sort = Sort.by(Sort.Order.desc("createdAt"));
        } else {
            throw new CustomException(ErrorCode.SORT_NOT_FOUND);
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Schedule> schedules = null;

        if (search == null) {
            schedules = scheduleRepository.findAll(pageable);
        } else {
            schedules = scheduleRepository.findAllBySearch(pageable, search);
        }

        List<ScheduleResponseDto> scheduleDtos = schedules.stream()
                .map(schedule -> new ScheduleResponseDto(schedule, getLikeResponseDto(user.getId(), schedule.getId())))
                .collect(Collectors.toList());

        return new PageImpl<>(scheduleDtos, pageable, schedules.getTotalElements());
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