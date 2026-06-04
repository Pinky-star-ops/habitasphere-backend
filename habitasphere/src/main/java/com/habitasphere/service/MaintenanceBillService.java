package com.habitasphere.service;

import com.habitasphere.dto.BillRequest;
import com.habitasphere.dto.MaintenanceBillResponse;

import java.util.List;

public interface MaintenanceBillService {

    MaintenanceBillResponse generateBill(BillRequest request);

    List<MaintenanceBillResponse> getAllBills();

    List<MaintenanceBillResponse> getBillsByResident(Long residentId);

    MaintenanceBillResponse markAsPaid(Long billId);

}
