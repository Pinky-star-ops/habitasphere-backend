package com.habitasphere.dto;

import com.habitasphere.enums.NoticePriority;
import com.habitasphere.enums.NoticeType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NoticeRequestDto {

    private String title;

    private String content;

    private NoticeType type;

    private NoticePriority priority;

    private LocalDate expiryDate;
    private boolean pinned;
}