package com.employee.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentStats {
    private String department;
    private Long employeeCount;
    private Double averagePerformance;
    private Double averageEngagement;
}
