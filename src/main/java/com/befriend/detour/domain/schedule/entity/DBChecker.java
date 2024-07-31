package com.befriend.detour.domain.schedule.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


@Component
public class DBChecker {

    @Autowired
    private DataSource dataSource;

    @Scheduled(fixedRate = 60000) // 60000ms = 1분
    public void checkDatabase() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(2)) {
                System.out.println("Database is up and running.");

                String sql = "SELECT id, hour_hits, hits FROM schedules";
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {

                    while (rs.next()) {
                        Long id = rs.getLong("id");
                        Long hourHits = rs.getLong("hour_hits");
                        Long hits = rs.getLong("hits");
                        System.out.println("Schedule ID: " + id + ", Hour Hits: " + hourHits + ", Hits: " + hits);
                    }
                } catch (SQLException e) {
                    System.err.println("Error querying database: " + e.getMessage());
                }

            } else {
                System.out.println("Database connection is not valid.");
            }
        } catch (SQLException e) {
            System.err.println("Error checking database connection: " + e.getMessage());
        }
    }
}