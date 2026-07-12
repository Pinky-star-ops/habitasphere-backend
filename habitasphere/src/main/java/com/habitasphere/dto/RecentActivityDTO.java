package com.habitasphere.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentActivityDTO {

    private String title;

    private String description;

    private LocalDateTime createdAt;
}