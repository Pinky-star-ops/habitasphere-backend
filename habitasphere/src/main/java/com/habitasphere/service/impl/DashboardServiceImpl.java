package com.habitasphere.service.impl;

import com.habitasphere.dto.AdminDashboardResponse;
import com.habitasphere.dto.ChartDataDTO;
import com.habitasphere.dto.DashboardSummaryDTO;
import com.habitasphere.dto.ResidentDashboardResponse;
import com.habitasphere.entity.RoleType;
import com.habitasphere.entity.User;
import com.habitasphere.enums.BookingStatus;
import com.habitasphere.enums.ComplaintStatus;
import com.habitasphere.enums.PaymentStatus;
import com.habitasphere.enums.VisitorStatus;
import com.habitasphere.repository.ComplaintRepository;
import com.habitasphere.repository.FacilityBookingRepository;
import com.habitasphere.repository.PaymentRepository;
import com.habitasphere.repository.UserRepository;
import com.habitasphere.repository.VisitorRepository;
import com.habitasphere.service.DashboardService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final ComplaintRepository complaintRepository;
    private final PaymentRepository paymentRepository;
    private final VisitorRepository visitorRepository;
    private final FacilityBookingRepository facilityBookingRepository;

    public DashboardServiceImpl(UserRepository userRepository, 
                                ComplaintRepository complaintRepository, 
                                PaymentRepository paymentRepository, 
                                VisitorRepository visitorRepository, 
                                FacilityBookingRepository facilityBookingRepository) {
        this.userRepository = userRepository;
        this.complaintRepository = complaintRepository;
        this.paymentRepository = paymentRepository;
        this.visitorRepository = visitorRepository;
        this.facilityBookingRepository = facilityBookingRepository;
    }

    @Override
    public AdminDashboardResponse getAdminDashboard() {
        long totalResidents = userRepository.countUsersByRole(RoleType.ROLE_RESIDENT);

        long totalComplaints = complaintRepository.count();
        long openComplaints = complaintRepository.countByStatus(ComplaintStatus.OPEN);
        long resolvedComplaints = complaintRepository.countByStatus(ComplaintStatus.RESOLVED);

        long totalVisitors = visitorRepository.count();
        long pendingVisitors = visitorRepository.countByStatus(VisitorStatus.PENDING);

        long totalPayments = paymentRepository.count();
        long pendingPayments = paymentRepository.countByStatus(PaymentStatus.PENDING);
        Double totalRevenue = paymentRepository.getTotalRevenue();
        if (totalRevenue == null) {
            totalRevenue = 0.0;
        }

        long totalBookings = facilityBookingRepository.count();
        long pendingBookings = facilityBookingRepository.countByStatus(BookingStatus.PENDING);

        List<DashboardSummaryDTO> summaryCards = buildSummaryCards(
                totalResidents, totalComplaints, openComplaints, totalVisitors, pendingVisitors,
                totalPayments, pendingPayments, totalRevenue, totalBookings, pendingBookings
        );

        List<ChartDataDTO> complaintChart = buildComplaintChart(totalComplaints, openComplaints, resolvedComplaints);
        List<ChartDataDTO> paymentChart = buildPaymentChart(totalPayments, pendingPayments);
        List<ChartDataDTO> bookingChart = buildBookingChart(totalBookings, pendingBookings);

        return AdminDashboardResponse.builder()
                .totalResidents(totalResidents)
                .totalComplaints(totalComplaints)
                .pendingComplaints(openComplaints)
                .resolvedComplaints(resolvedComplaints)
                .totalVisitors(totalVisitors)
                .pendingVisitors(pendingVisitors)
                .totalPayments(totalPayments)
                .pendingPayments(pendingPayments)
                .totalBookings(totalBookings)
                .pendingBookings(pendingBookings)
                .totalRevenue(totalRevenue)
                .summaryCards(summaryCards)
                .complaintChart(complaintChart)
                .paymentChart(paymentChart)
                .bookingChart(bookingChart)
                .recentActivities(Collections.emptyList())
                .build();
    }

    @Override
    public ResidentDashboardResponse getResidentDashboard(String email) {
        User resident = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Long residentId = resident.getId();

        long myComplaints = complaintRepository.countByResidentId(residentId);
        long openComplaints = complaintRepository.countByResidentIdAndStatus(residentId, ComplaintStatus.OPEN);
        long resolvedComplaints = complaintRepository.countByResidentIdAndStatus(residentId, ComplaintStatus.RESOLVED);

        long pendingPayments = paymentRepository.countByResidentIdAndStatus(residentId, PaymentStatus.PENDING);

        long approvedVisitors = visitorRepository.countByCreatedByIdAndStatus(residentId, VisitorStatus.APPROVED);

        long myBookings = facilityBookingRepository.countByUserId(residentId);

        List<DashboardSummaryDTO> summaryCards = new ArrayList<>();
        summaryCards.add(DashboardSummaryDTO.builder().title("My Complaints").value(myComplaints).build());
        summaryCards.add(DashboardSummaryDTO.builder().title("Open Complaints").value(openComplaints).build());
        summaryCards.add(DashboardSummaryDTO.builder().title("Resolved Complaints").value(resolvedComplaints).build());
        summaryCards.add(DashboardSummaryDTO.builder().title("Pending Payments").value(pendingPayments).build());
        summaryCards.add(DashboardSummaryDTO.builder().title("Approved Visitors").value(approvedVisitors).build());
        summaryCards.add(DashboardSummaryDTO.builder().title("My Bookings").value(myBookings).build());

        return ResidentDashboardResponse.builder()
                .myComplaints(myComplaints)
                .pendingComplaints(openComplaints)
                .resolvedComplaints(resolvedComplaints)
                .pendingPayments(pendingPayments)
                .approvedVisitors(approvedVisitors)
                .myBookings(myBookings)
                .summaryCards(summaryCards)
                .recentActivities(Collections.emptyList())
                .build();
    }

    private List<DashboardSummaryDTO> buildSummaryCards(long totalResidents, long totalComplaints, long openComplaints,
                                                   long totalVisitors, long pendingVisitors,
                                                   long totalPayments, long pendingPayments, Double totalRevenue,
                                                   long totalBookings, long pendingBookings) {
        List<DashboardSummaryDTO> cards = new ArrayList<>();
        cards.add(DashboardSummaryDTO.builder().title("Total Residents").value(totalResidents).build());
        cards.add(DashboardSummaryDTO.builder().title("Total Complaints").value(totalComplaints).build());
        cards.add(DashboardSummaryDTO.builder().title("Open Complaints").value(openComplaints).build());
        cards.add(DashboardSummaryDTO.builder().title("Total Visitors").value(totalVisitors).build());
        cards.add(DashboardSummaryDTO.builder().title("Pending Visitors").value(pendingVisitors).build());
        cards.add(DashboardSummaryDTO.builder().title("Total Payments").value(totalPayments).build());
        cards.add(DashboardSummaryDTO.builder().title("Pending Payments").value(pendingPayments).build());
        cards.add(DashboardSummaryDTO.builder().title("Total Revenue").value(totalRevenue.longValue()).build());
        cards.add(DashboardSummaryDTO.builder().title("Total Bookings").value(totalBookings).build());
        cards.add(DashboardSummaryDTO.builder().title("Pending Bookings").value(pendingBookings).build());
        return cards;
    }

    private List<ChartDataDTO> buildComplaintChart(long total, long open, long resolved) {
        return Arrays.asList(
                ChartDataDTO.builder().label("Total").value(total).build(),
                ChartDataDTO.builder().label("Open").value(open).build(),
                ChartDataDTO.builder().label("Resolved").value(resolved).build()
        );
    }

    private List<ChartDataDTO> buildPaymentChart(long total, long pending) {
        return Arrays.asList(
                ChartDataDTO.builder().label("Total").value(total).build(),
                ChartDataDTO.builder().label("Pending").value(pending).build()
        );
    }

    private List<ChartDataDTO> buildBookingChart(long total, long pending) {
        return Arrays.asList(
                ChartDataDTO.builder().label("Total").value(total).build(),
                ChartDataDTO.builder().label("Pending").value(pending).build()
        );
    }
}
