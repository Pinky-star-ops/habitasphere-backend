package com.habitasphere.dto;

import com.habitasphere.enums.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponseDto {

    private Long id;

    private String title;

    private String message;

    private NotificationType type;

    private boolean isRead;

    private LocalDateTime createdAt;
}