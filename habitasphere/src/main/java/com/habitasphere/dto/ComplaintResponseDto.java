package com.habitasphere.dto;

import com.habitasphere.enums.ComplaintPriority;
import com.habitasphere.enums.ComplaintStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ComplaintResponseDto {

    private Long id;

    private String title;

    private String description;

    private ComplaintStatus status;

    private ComplaintPriority priority;

    private LocalDateTime createdAt;

    private LocalDateTime resolvedAt;

    private String residentName;

    private String apartmentNumber;
}