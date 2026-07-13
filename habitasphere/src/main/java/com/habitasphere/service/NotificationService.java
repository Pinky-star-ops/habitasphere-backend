package com.habitasphere.service;

import com.habitasphere.dto.NotificationResponseDto;
import com.habitasphere.entity.User;
import com.habitasphere.enums.NotificationType;

import java.util.List;

public interface NotificationService {

    List<NotificationResponseDto> getMyNotifications();

    List<NotificationResponseDto> getUnreadNotifications();

    NotificationResponseDto markAsRead(Long id);

    void createNotification(
            User user,
            String title,
            String message,
            NotificationType type
    );
}