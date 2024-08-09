package com.befriend.detour.domain.schedule.dao;

import com.befriend.detour.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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

    public List<Long> getRanking() {
        List<Long> ranking = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            String key = String.valueOf(i);
            String value = stringRedisTemplate.opsForValue().get(key);

            if (value != null) {
                try {
                    ranking.add(Long.parseLong(value));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number format for key " + key);
                }
            }
        }

        return ranking;
    }

}