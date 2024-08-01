package com.befriend.detour.domain.schedule.dao;

import com.befriend.detour.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class RankingDao {

    private final StringRedisTemplate stringRedisTemplate;
    private final ScheduleService scheduleService;

    public void saveRanking(List<Long> scheduleIds) {
        for (int i = 0; i < scheduleIds.size(); i++) {
            stringRedisTemplate.opsForValue()
                    .set(String.valueOf(i + 1), String.valueOf(scheduleIds.get(i)));
        }
        scheduleService.deleteAllHourHits();
    }

}