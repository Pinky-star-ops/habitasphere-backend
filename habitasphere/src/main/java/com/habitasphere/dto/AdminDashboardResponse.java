package com.habitasphere.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponse {

    private Long totalResidents;

    private Long totalComplaints;

    private Long pendingComplaints;

    private Long resolvedComplaints;

    private Long totalVisitors;

    private Long pendingVisitors;

    private Long totalPayments;

    private Long pendingPayments;

    private Long totalBookings;

    private Long pendingBookings;

    private Long totalParcels;

    private Long pendingParcels;

    private Long collectedParcels;

    private Double totalRevenue;

    private List<DashboardSummaryDTO> summaryCards;

    private List<ChartDataDTO> complaintChart;

    private List<ChartDataDTO> paymentChart;

    private List<ChartDataDTO> bookingChart;

    private List<RecentActivityDTO> recentActivities;
}