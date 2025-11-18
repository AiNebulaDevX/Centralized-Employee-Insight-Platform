package com.employee.userservice.service;

import com.employee.userservice.dto.PerformanceRecordDTO;
import com.employee.userservice.kafka.KafkaProducerService;
import com.employee.userservice.kafka.UserEvent;
import com.employee.userservice.model.PerformanceRecord;
import com.employee.userservice.model.User;
import com.employee.userservice.repository.PerformanceRecordRepository;
import com.employee.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PerformanceService {
    
    private final PerformanceRecordRepository performanceRecordRepository;
    private final UserRepository userRepository;
    private final KafkaProducerService kafkaProducerService;
    
    public PerformanceRecordDTO createPerformanceRecord(PerformanceRecordDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        PerformanceRecord record = PerformanceRecord.builder()
                .userId(dto.getUserId())
                .score(dto.getScore())
                .feedback(dto.getFeedback())
                .quarter(dto.getQuarter())
                .year(dto.getYear())
                .build();
        
        record = performanceRecordRepository.save(record);
        
        // Update user performance score
        user.setPerformanceScore(dto.getScore());
        userRepository.save(user);
        
        // Send Kafka event
        UserEvent event = UserEvent.builder()
                .eventType("PERFORMANCE_UPDATED")
                .userId(user.getId())
                .email(user.getEmail())
                .performanceScore(dto.getScore())
                .timestamp(LocalDateTime.now())
                .build();
        kafkaProducerService.sendUserEvent("user-events", event);
        
        return convertToDTO(record);
    }
    
    public List<PerformanceRecordDTO> getPerformanceByUserId(Long userId) {
        return performanceRecordRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private PerformanceRecordDTO convertToDTO(PerformanceRecord record) {
        return PerformanceRecordDTO.builder()
                .id(record.getId())
                .userId(record.getUserId())
                .score(record.getScore())
                .feedback(record.getFeedback())
                .quarter(record.getQuarter())
                .year(record.getYear())
                .createdAt(record.getCreatedAt())
                .build();
    }
}
