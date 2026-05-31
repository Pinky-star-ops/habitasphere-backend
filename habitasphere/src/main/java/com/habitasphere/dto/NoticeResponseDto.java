package com.habitasphere.dto;

import com.habitasphere.enums.NoticePriority;
import com.habitasphere.enums.NoticeType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class NoticeResponseDto {

    private Long id;

    private String title;

    private String content;

    private NoticeType type;

    private NoticePriority priority;

    private LocalDateTime createdAt;

    private LocalDate expiryDate;

    private boolean active;

    private String createdBy;
}