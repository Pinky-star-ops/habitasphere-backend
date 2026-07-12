package com.habitasphere.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDTO {

    private String title;

    private Long value;

    private String icon;

    private String color;
}