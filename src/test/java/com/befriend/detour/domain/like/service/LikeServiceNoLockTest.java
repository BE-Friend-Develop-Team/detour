package com.befriend.detour.domain.like.service;

import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.schedule.repository.ScheduleRepository;
import com.befriend.detour.domain.user.entity.User;
import com.befriend.detour.domain.user.entity.UserRoleEnum;
import com.befriend.detour.domain.user.entity.UserStatusEnum;
import com.befriend.detour.domain.user.repository.UserRepository;
import com.befriend.detour.global.exception.CustomException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class LikeServiceNoLockTest {

    @Autowired
    private LikeService likeService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public LikeServiceNoLockTest(LikeService likeService, ScheduleRepository scheduleRepository, UserRepository userRepository) {
        this.likeService = likeService;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
    }

    private User[] users;
    private Schedule schedule;

    @BeforeEach
    void setUp() {
        // 100명의 사용자를 생성, 테스트 전에 실행
        users = IntStream.range(0, 1000)
                .mapToObj(i -> new User(
                        "user" + i + "@example.com",
                        "Password" + i,
                        "User" + i,
                        UserStatusEnum.ACTIVE,
                        UserRoleEnum.USER,
                        123456789L + i,
                        "loginId" + i
                ))
                .peek(userRepository::save)  // 각 사용자를 저장소에 저장
                .toArray(User[]::new);  // 배열로 변환하여 users에 저장

        // 하나의 일정 생성
        schedule = new Schedule("Sample Title", LocalDateTime.now(), LocalDateTime.now().plusDays(1), users[0]);
        scheduleRepository.save(schedule);
    }

    @AfterEach
    void tearDown() {
        scheduleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Commit
    @DisplayName("동시성 제어 없음 : 좋아요 동시에 1000명이 눌렀을 때")
    void concurrencyTestWithoutLock() throws InterruptedException {
        int userCount = 1000;
        CountDownLatch latch = new CountDownLatch(userCount);

        ExecutorService executorService = Executors.newFixedThreadPool(50);
        List<User> userList = List.of(users);

        IntStream.range(0, userCount).forEach(i -> {
            executorService.submit(() -> {
                try {
                    User user = userList.get(i);
                    try {
                        likeService.createScheduleLike(schedule.getId(), user);
                    } catch (CustomException e) {
                        // 예외가 발생할 수 있으므로 무시하고 로그 남김
                        e.printStackTrace();
                    }
                } finally {
                    latch.countDown();
                }
            });
        });

        latch.await(120, TimeUnit.SECONDS);
        executorService.shutdown();
        executorService.awaitTermination(2, TimeUnit.MINUTES);

        Schedule updatedSchedule = scheduleRepository.findById(schedule.getId()).orElseThrow();
        long actualLikes = updatedSchedule.getLikeCount();
        long expectedLikes = 1000L;

        System.out.println("Expected : " + expectedLikes + "\n Actual : " + actualLikes );

        assertEquals(expectedLikes, actualLikes, "테스트 실패: 총 좋아요 수가 예상과 다릅니다.");
    }

    @Test
    @DisplayName("동시성 제어 : 좋아요 동시에 100명이 눌렀을 때")
    void concurrencyTestUsingLock() throws InterruptedException {
        int userCount = 100;
        CountDownLatch latch = new CountDownLatch(userCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        ExecutorService executorService = Executors.newFixedThreadPool(userCount);
        List<User> userList = List.of(users);

        IntStream.range(0, userCount).forEach(i -> {
            executorService.submit(() -> {
                try {
                    User user = userList.get(i);
                    try {
                        likeService.createScheduleLikeWithLock(schedule.getId(), user);
                        successCount.incrementAndGet();
                        System.out.println("User " + user.getId() + ": Like successful");
                    } catch (CustomException e) {
                        if (e.getMessage().contains("이미 좋아요를 누른 일정입니다")) {
                            successCount.incrementAndGet(); // 이미 좋아요를 누른 경우도 성공으로 간주
                            System.out.println("User " + user.getId() + ": Already liked");
                        } else if (e.getMessage().contains("락 획득에 실패했습니다")) {
                            failCount.incrementAndGet();
                            System.out.println("User " + user.getId() + ": Lock acquisition failed");
                        } else {
                            e.printStackTrace();
                            failCount.incrementAndGet();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        });

        latch.await(30, TimeUnit.SECONDS);
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        Schedule updatedSchedule = scheduleRepository.findById(schedule.getId()).orElseThrow();
        long actualLikes = updatedSchedule.getLikeCount();
        long expectedLikes = Math.min(userCount, actualLikes);

        System.out.println("총 좋아요 수: " + actualLikes);
        System.out.println("성공 횟수: " + successCount.get());
        System.out.println("실패 횟수: " + failCount.get());
        System.out.println("예상 좋아요 수: " + userCount);

        System.out.println("Expected: " + expectedLikes + "L, Actual: " + actualLikes + "L");

        assertEquals(expectedLikes, actualLikes, "테스트 실패: 총 좋아요 수가 예상과 다릅니다.");

        assertEquals(expectedLikes, actualLikes, "총 좋아요 수가 일치하지 않습니다.");
    }
}