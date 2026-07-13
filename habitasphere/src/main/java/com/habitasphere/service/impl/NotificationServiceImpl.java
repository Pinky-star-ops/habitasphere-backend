package com.habitasphere.service.impl;

import com.habitasphere.dto.NotificationResponseDto;
import com.habitasphere.entity.Notification;
import com.habitasphere.entity.User;
import com.habitasphere.enums.NotificationType;
import com.habitasphere.repository.NotificationRepository;
import com.habitasphere.repository.UserRepository;
import com.habitasphere.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public List<NotificationResponseDto> getMyNotifications() {

        User user = getCurrentUser();

        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<NotificationResponseDto> getUnreadNotifications() {

        User user = getCurrentUser();

        return notificationRepository
                .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public NotificationResponseDto markAsRead(Long id) {

        User user = getCurrentUser();

        Notification notification =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Notification not found"));

        if (!notification.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "You are not authorized to access this notification");
        }

        notification.setRead(true);

        return mapToDto(
                notificationRepository.save(notification)
        );
    }

    @Override
    public void createNotification(
            User user,
            String title,
            String message,
            NotificationType type
    ) {

        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .message(message)
                .type(type)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }

    private User getCurrentUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "User not found"));
    }

    private NotificationResponseDto mapToDto(
            Notification notification
    ) {

        return NotificationResponseDto.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}