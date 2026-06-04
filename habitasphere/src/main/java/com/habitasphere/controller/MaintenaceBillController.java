package com.habitasphere.controller;

import com.habitasphere.dto.BillRequest;
import com.habitasphere.dto.MaintenanceBillResponse;
import com.habitasphere.service.MaintenanceBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class MaintenaceBillController {
    private final MaintenanceBillService billService;

    @PostMapping
    public MaintenanceBillResponse generateBill(
            @RequestBody BillRequest request) {

        return billService.generateBill(request);
    }

    @GetMapping
    public List<MaintenanceBillResponse> getAllBills() {
        return billService.getAllBills();
    }

    @GetMapping("/resident/{residentId}")
    public List<MaintenanceBillResponse> getResidentBills(
            @PathVariable Long residentId) {

        return billService.getBillsByResident(residentId);
    }

    @PutMapping("/{billId}/pay")
    public MaintenanceBillResponse markPaid(
            @PathVariable Long billId) {

        return billService.markAsPaid(billId);
    }
}
