package com.befriend.detour.domain.schedule.entity;

import com.befriend.detour.domain.schedule.dao.RankingDao;
import com.befriend.detour.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DBChecker {

    private final DataSource dataSource;
    private final ScheduleService scheduleService;
    private final RankingDao rankingDao;

    @Scheduled(fixedRate = 3600000)
    public void checkDatabase() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(2)) {

                List<Long> scheduleIds = scheduleService.getRanking();

                if (scheduleIds != null && !scheduleIds.isEmpty()) {
                    System.out.println(scheduleIds);
                    rankingDao.saveRanking(scheduleIds);
                } else {
                    System.out.println("No schedule IDs found.");
                }
            } else {
                System.out.println("Database connection is not valid.");
            }
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error occurred: " + e.getMessage());
        }
    }

}