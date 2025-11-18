package com.employee.analytics.service;

import com.employee.analytics.model.AnalyticsRecord;
import com.employee.analytics.model.DepartmentStats;
import com.employee.analytics.model.PerformanceTrend;
import com.employee.analytics.repository.AnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    
    private final AnalyticsRepository analyticsRepository;
    
    public List<AnalyticsRecord> getAllRecords() {
        return analyticsRepository.findAll();
    }
    
    public List<AnalyticsRecord> getRecordsByUserId(Long userId) {
        return analyticsRepository.findByUserId(userId);
    }
    
    public List<AnalyticsRecord> getRecordsByDepartment(String department) {
        return analyticsRepository.findByDepartment(department);
    }
    
    public Map<String, Object> getPerformanceAnalytics() {
        List<AnalyticsRecord> records = analyticsRepository.findAll();
        
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("totalRecords", records.size());
        analytics.put("averagePerformance", calculateAveragePerformance(records));
        analytics.put("performanceTrends", getPerformanceTrends());
        
        return analytics;
    }
    
    public Map<String, Object> getEngagementMetrics() {
        List<AnalyticsRecord> records = analyticsRepository.findAll();
        
        Map<String, Long> eventCounts = records.stream()
                .collect(Collectors.groupingBy(
                        AnalyticsRecord::getEventType,
                        Collectors.counting()
                ));
        
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalEvents", records.size());
        metrics.put("eventBreakdown", eventCounts);
        metrics.put("activeUsers", records.stream()
                .map(AnalyticsRecord::getUserId)
                .distinct()
                .count());
        
        return metrics;
    }
    
    public List<DepartmentStats> getDepartmentStatistics() {
        List<String> departments = analyticsRepository.findAll().stream()
                .map(AnalyticsRecord::getDepartment)
                .filter(d -> d != null)
                .distinct()
                .collect(Collectors.toList());
        
        return departments.stream()
                .map(dept -> DepartmentStats.builder()
                        .department(dept)
                        .employeeCount(analyticsRepository.countUsersByDepartment(dept))
                        .averagePerformance(analyticsRepository.getAveragePerformanceByDepartment(dept))
                        .build())
                .collect(Collectors.toList());
    }
    
    private Double calculateAveragePerformance(List<AnalyticsRecord> records) {
        return records.stream()
                .filter(r -> r.getPerformanceScore() != null)
                .mapToDouble(AnalyticsRecord::getPerformanceScore)
                .average()
                .orElse(0.0);
    }
    
    private List<PerformanceTrend> getPerformanceTrends() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<AnalyticsRecord> recentRecords = analyticsRepository
                .findByTimestampBetween(thirtyDaysAgo, LocalDateTime.now());
        
        Map<LocalDateTime, List<AnalyticsRecord>> groupedByDate = recentRecords.stream()
                .collect(Collectors.groupingBy(r -> r.getTimestamp().toLocalDate().atStartOfDay()));
        
        return groupedByDate.entrySet().stream()
                .map(entry -> PerformanceTrend.builder()
                        .date(entry.getKey())
                        .averageScore(entry.getValue().stream()
                                .filter(r -> r.getPerformanceScore() != null)
                                .mapToDouble(AnalyticsRecord::getPerformanceScore)
                                .average()
                                .orElse(0.0))
                        .recordCount((long) entry.getValue().size())
                        .build())
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .collect(Collectors.toList());
    }
}
