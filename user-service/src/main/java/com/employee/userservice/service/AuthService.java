package com.employee.userservice.service;

import com.employee.userservice.dto.AuthResponse;
import com.employee.userservice.dto.LoginRequest;
import com.employee.userservice.dto.RegisterRequest;
import com.employee.userservice.kafka.KafkaProducerService;
import com.employee.userservice.kafka.UserEvent;
import com.employee.userservice.model.User;
import com.employee.userservice.repository.UserRepository;
import com.employee.userservice.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final KafkaProducerService kafkaProducerService;
    
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .department(request.getDepartment())
                .position(request.getPosition())
                .role(request.getRole())
                .performanceScore(0.0)
                .engagementLevel(0)
                .active(true)
                .build();
        
        user = userRepository.save(user);
        
        // Send Kafka event
        UserEvent event = UserEvent.builder()
                .eventType("USER_REGISTERED")
                .userId(user.getId())
                .email(user.getEmail())
                .department(user.getDepartment())
                .timestamp(LocalDateTime.now())
                .build();
        kafkaProducerService.sendUserEvent("user-events", event);
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        
        return AuthResponse.builder()
                .token(token)
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .build();
    }
    
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        
        // Send Kafka event
        UserEvent event = UserEvent.builder()
                .eventType("USER_LOGIN")
                .userId(user.getId())
                .email(user.getEmail())
                .timestamp(LocalDateTime.now())
                .build();
        kafkaProducerService.sendUserEvent("user-events", event);
        
        return AuthResponse.builder()
                .token(token)
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .build();
    }
}
