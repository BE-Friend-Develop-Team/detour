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
        for (int i = 1; i <= 12; i++) {  // 상위 12개 랭킹을 가져옵니다
            String value = stringRedisTemplate.opsForValue().get(String.valueOf(i));
            if (value != null) {
                ranking.add(Long.parseLong(value));
            }
        }
        return ranking;
    }

}