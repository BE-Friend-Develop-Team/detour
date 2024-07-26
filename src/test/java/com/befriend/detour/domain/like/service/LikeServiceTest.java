package com.befriend.detour.domain.like.service;

import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.schedule.repository.ScheduleRepository;
import com.befriend.detour.domain.schedule.service.ScheduleService;
import com.befriend.detour.domain.user.entity.User;
import com.befriend.detour.domain.user.entity.UserRoleEnum;
import com.befriend.detour.domain.user.entity.UserStatusEnum;
import com.befriend.detour.domain.user.repository.UserRepository;
import com.befriend.detour.global.redis.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class LikeServiceTest {

    @Autowired
    private LikeService likeService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisService redisService;

    private Schedule schedule;
    private User user;

    @BeforeEach
    public void setup() {
        user = userRepository.save(new User("test@example.com", "encodedPassword", "nickname", UserStatusEnum.ACTIVE, UserRoleEnum.USER, null));
        Date now = new Date();
        schedule = scheduleRepository.save(new Schedule("Test Schedule", now, now, user));
    }

    @Test
    public void testConcurrentLikes() throws InterruptedException {
        int numberOfUsers = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfUsers);
        CountDownLatch latch = new CountDownLatch(numberOfUsers);

        for (int i = 0; i < numberOfUsers; i++) {
            final int index = i;
            executorService.submit(() -> {
                try {
                    User user = userRepository.save(new User("testUser" + index + "@example.com", "encodedPassword", "nickname" + index, UserStatusEnum.ACTIVE, UserRoleEnum.USER, null));
                    likeService.createScheduleLike(schedule.getId(), user);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        Schedule updatedSchedule = scheduleService.findById(schedule.getId());
        assertThat(updatedSchedule.getLikeCount()).isEqualTo(numberOfUsers);
    }
}