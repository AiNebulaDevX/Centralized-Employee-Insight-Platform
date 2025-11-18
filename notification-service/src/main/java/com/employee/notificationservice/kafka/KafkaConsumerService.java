package com.employee.notificationservice.kafka;

import com.employee.notificationservice.model.Notification;
import com.employee.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void handleUserEvent(UserEvent event) {
        log.info("Processing user event: {}", event);
        
        // Create notification based on event type
        Notification notification = createNotificationFromEvent(event);
        notificationService.saveNotification(notification);
        
        // Send real-time notification via WebSocket
        messagingTemplate.convertAndSendToUser(
                event.getUserId().toString(),
                "/queue/notifications",
                notification
        );
        
        // Send email for important events
        if (isImportantEvent(event)) {
            notificationService.sendEmailNotification(notification, event.getEmail());
        }
    }
    
    private Notification createNotificationFromEvent(UserEvent event) {
        String title = "System Notification";
        String message = "Event: " + event.getEventType();
        
        return Notification.builder()
                .userId(event.getUserId().toString())
                .title(title)
                .message(message)
                .type(getNotificationType(event.getEventType()))
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();
    }
    
    private Notification.NotificationType getNotificationType(String eventType) {
        return switch (eventType) {
            case "USER_REGISTERED" -> Notification.NotificationType.INFO;
            case "USER_LOGIN" -> Notification.NotificationType.INFO;
            case "PERFORMANCE_UPDATED" -> Notification.NotificationType.PERFORMANCE_UPDATE;
            case "USER_DELETED" -> Notification.NotificationType.ALERT;
            default -> Notification.NotificationType.SYSTEM;
        };
    }
    
    private boolean isImportantEvent(UserEvent event) {
        return event.getEventType().equals("PERFORMANCE_UPDATED") || 
               event.getEventType().equals("USER_DELETED");
    }
}
