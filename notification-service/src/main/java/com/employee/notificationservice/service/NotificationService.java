package com.employee.notificationservice.service;

import com.employee.notificationservice.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.List;
import java.util.UUID;

public interface NotificationService {
    Notification saveNotification(Notification notification);
    List<Notification> getUserNotifications(String userId);
    void sendEmailNotification(Notification notification, String email);
    void markAsRead(String notificationId);
}

@Service
@RequiredArgsConstructor
@Slf4j
class NotificationServiceImpl implements NotificationService {
    
    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;
    private final String fromEmail;
    private final String adminEmail;

    @Override
    public Notification saveNotification(Notification notification) {
        // In a real app, save to database
        notification.setId(UUID.randomUUID().toString());
        log.info("Saving notification: {}", notification);
        return notification;
    }

    @Override
    public List<Notification> getUserNotifications(String userId) {
        // In a real app, fetch from database
        return List.of();
    }

    @Override
    @Async
    public void sendEmailNotification(Notification notification, String toEmail) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            // Prepare the evaluation context
            Context context = new Context();
            context.setVariable("title", notification.getTitle());
            context.setVariable("message", notification.getMessage());
            
            // Process the HTML template
            String htmlContent = templateEngine.process("email/notification", context);
            
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(notification.getTitle());
            helper.setText(htmlContent, true);
            
            emailSender.send(message);
            log.info("Email notification sent to: {}", toEmail);
        } catch (MessagingException e) {
            log.error("Failed to send email notification", e);
            // Fallback to simple email
            sendSimpleEmail(notification, toEmail);
        }
    }
    
    private void sendSimpleEmail(Notification notification, String toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(notification.getTitle());
        message.setText(notification.getMessage());
        
        try {
            emailSender.send(message);
            log.info("Simple email notification sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send simple email notification", e);
        }
    }

    @Override
    public void markAsRead(String notificationId) {
        // In a real app, update in database
        log.info("Marking notification as read: {}", notificationId);
    }
}
