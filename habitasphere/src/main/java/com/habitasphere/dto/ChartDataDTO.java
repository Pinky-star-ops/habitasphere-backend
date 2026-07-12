package com.habitasphere.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartDataDTO {

    private String label;

    private Long value;
}