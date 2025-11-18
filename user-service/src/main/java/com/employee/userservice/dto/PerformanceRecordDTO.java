package com.employee.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceRecordDTO {
    private Long id;
    private Long userId;
    private Double score;
    private String feedback;
    private String quarter;
    private Integer year;
    private LocalDateTime createdAt;
}
