package com.employee.analytics.controller;

import com.employee.analytics.model.AnalyticsRecord;
import com.employee.analytics.model.DepartmentStats;
import com.employee.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AnalyticsController {
    
    private final AnalyticsService analyticsService;
    
    @GetMapping("/records")
    public ResponseEntity<List<AnalyticsRecord>> getAllRecords() {
        return ResponseEntity.ok(analyticsService.getAllRecords());
    }
    
    @GetMapping("/records/user/{userId}")
    public ResponseEntity<List<AnalyticsRecord>> getRecordsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(analyticsService.getRecordsByUserId(userId));
    }
    
    @GetMapping("/records/department/{department}")
    public ResponseEntity<List<AnalyticsRecord>> getRecordsByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(analyticsService.getRecordsByDepartment(department));
    }
    
    @GetMapping("/performance")
    public ResponseEntity<Map<String, Object>> getPerformanceAnalytics() {
        return ResponseEntity.ok(analyticsService.getPerformanceAnalytics());
    }
    
    @GetMapping("/engagement")
    public ResponseEntity<Map<String, Object>> getEngagementMetrics() {
        return ResponseEntity.ok(analyticsService.getEngagementMetrics());
    }
    
    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentStats>> getDepartmentStatistics() {
        return ResponseEntity.ok(analyticsService.getDepartmentStatistics());
    }
}
