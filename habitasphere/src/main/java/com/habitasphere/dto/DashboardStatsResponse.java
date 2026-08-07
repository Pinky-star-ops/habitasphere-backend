package com.habitasphere.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatsResponse {

    private Double totalCollected;
    private Double pendingAmount;
    private Long totalDefaulters;
    private Double monthlyRevenue;

}
