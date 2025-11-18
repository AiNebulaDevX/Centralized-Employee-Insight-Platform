package com.employee.userservice.service;

import com.employee.userservice.dto.UserDTO;
import com.employee.userservice.kafka.KafkaProducerService;
import com.employee.userservice.kafka.UserEvent;
import com.employee.userservice.model.User;
import com.employee.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final KafkaProducerService kafkaProducerService;
    
    @Cacheable(value = "users", key = "#id")
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(user);
    }
    
    @Cacheable(value = "users")
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<UserDTO> getUsersByDepartment(String department) {
        return userRepository.findByDepartment(department).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @CacheEvict(value = "users", allEntries = true)
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setDepartment(userDTO.getDepartment());
        user.setPosition(userDTO.getPosition());
        user.setPerformanceScore(userDTO.getPerformanceScore());
        user.setEngagementLevel(userDTO.getEngagementLevel());
        
        user = userRepository.save(user);
        
        // Send Kafka event
        UserEvent event = UserEvent.builder()
                .eventType("USER_UPDATED")
                .userId(user.getId())
                .email(user.getEmail())
                .department(user.getDepartment())
                .performanceScore(user.getPerformanceScore())
                .timestamp(LocalDateTime.now())
                .build();
        kafkaProducerService.sendUserEvent("user-events", event);
        
        return convertToDTO(user);
    }
    
    @CacheEvict(value = "users", allEntries = true)
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(false);
        userRepository.save(user);
        
        // Send Kafka event
        UserEvent event = UserEvent.builder()
                .eventType("USER_DELETED")
                .userId(user.getId())
                .email(user.getEmail())
                .timestamp(LocalDateTime.now())
                .build();
        kafkaProducerService.sendUserEvent("user-events", event);
    }
    
    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .department(user.getDepartment())
                .position(user.getPosition())
                .role(user.getRole().name())
                .performanceScore(user.getPerformanceScore())
                .engagementLevel(user.getEngagementLevel())
                .createdAt(user.getCreatedAt())
                .active(user.getActive())
                .build();
    }
}
