package com.habitasphere.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResidentDashboardResponse {

    private Long myComplaints;

    private Long pendingComplaints;

    private Long resolvedComplaints;

    private Long myBookings;

    private Long pendingPayments;

    private Long approvedVisitors;

    private List<DashboardSummaryDTO> summaryCards;

    private List<RecentActivityDTO> recentActivities;
}