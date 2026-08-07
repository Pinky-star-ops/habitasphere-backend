package com.habitasphere.service;

import com.habitasphere.dto.BillRequest;
import com.habitasphere.dto.DashboardStatsResponse;
import com.habitasphere.dto.MaintenanceBillResponse;

import java.util.List;

public interface MaintenanceBillService {

    MaintenanceBillResponse generateBill(BillRequest request);

    List<MaintenanceBillResponse> generateMonthlyBills(BillRequest request);

    List<MaintenanceBillResponse> getAllBills();

    List<MaintenanceBillResponse> getBillsByResident(Long residentId);

    MaintenanceBillResponse markAsPaid(Long billId);

    List<MaintenanceBillResponse> getDefaulters();

    MaintenanceBillResponse updateBill(Long billId, BillRequest request);

    List<MaintenanceBillResponse> getMyBills();

    MaintenanceBillResponse getBillDetails(Long billId);

    DashboardStatsResponse getDashboardStats();

}

