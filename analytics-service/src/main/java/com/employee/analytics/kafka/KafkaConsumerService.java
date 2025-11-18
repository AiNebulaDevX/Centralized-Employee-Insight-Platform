package com.employee.analytics.kafka;

import com.employee.analytics.model.AnalyticsRecord;
import com.employee.analytics.repository.AnalyticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {
    
    private final AnalyticsRepository analyticsRepository;
    
    @KafkaListener(topics = "user-events", groupId = "analytics-group")
    public void consumeUserEvent(UserEvent event) {
        log.info("Received user event: {}", event);
        
        AnalyticsRecord record = AnalyticsRecord.builder()
                .eventType(event.getEventType())
                .userId(event.getUserId())
                .email(event.getEmail())
                .department(event.getDepartment())
                .performanceScore(event.getPerformanceScore())
                .build();
        
        analyticsRepository.save(record);
        log.info("Analytics record saved: {}", record);
    }
}
