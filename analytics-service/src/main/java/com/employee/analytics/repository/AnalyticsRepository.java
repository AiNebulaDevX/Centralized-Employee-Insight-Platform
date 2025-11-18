package com.employee.analytics.repository;

import com.employee.analytics.model.AnalyticsRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnalyticsRepository extends JpaRepository<AnalyticsRecord, Long> {
    List<AnalyticsRecord> findByUserId(Long userId);
    List<AnalyticsRecord> findByDepartment(String department);
    List<AnalyticsRecord> findByEventType(String eventType);
    List<AnalyticsRecord> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT AVG(a.performanceScore) FROM AnalyticsRecord a WHERE a.department = :department AND a.performanceScore IS NOT NULL")
    Double getAveragePerformanceByDepartment(String department);
    
    @Query("SELECT COUNT(DISTINCT a.userId) FROM AnalyticsRecord a WHERE a.department = :department")
    Long countUsersByDepartment(String department);
}
