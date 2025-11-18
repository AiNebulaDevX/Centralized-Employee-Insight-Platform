package com.employee.userservice.repository;

import com.employee.userservice.model.PerformanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceRecordRepository extends JpaRepository<PerformanceRecord, Long> {
    List<PerformanceRecord> findByUserId(Long userId);
    List<PerformanceRecord> findByUserIdAndYear(Long userId, Integer year);
}
