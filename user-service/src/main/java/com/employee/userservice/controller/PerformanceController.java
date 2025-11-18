package com.employee.userservice.controller;

import com.employee.userservice.dto.PerformanceRecordDTO;
import com.employee.userservice.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/performance")
@RequiredArgsConstructor
public class PerformanceController {
    
    private final PerformanceService performanceService;
    
    @PostMapping
    public ResponseEntity<PerformanceRecordDTO> createPerformanceRecord(@RequestBody PerformanceRecordDTO dto) {
        return ResponseEntity.ok(performanceService.createPerformanceRecord(dto));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PerformanceRecordDTO>> getPerformanceByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(performanceService.getPerformanceByUserId(userId));
    }
}
