package com.habitasphere.controller;

import com.habitasphere.dto.BillRequest;
import com.habitasphere.dto.DashboardStatsResponse;
import com.habitasphere.dto.MaintenanceBillResponse;
import com.habitasphere.service.MaintenanceBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MaintenanceBillController {

    private final MaintenanceBillService billService;

    // --- Admin APIs ---

    @PostMapping("/admin/maintenance/generate")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARY')")
    public ResponseEntity<MaintenanceBillResponse> generateBill(@RequestBody BillRequest request) {
        return ResponseEntity.ok(billService.generateBill(request));
    }

    @GetMapping("/admin/maintenance/bills")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARY')")
    public ResponseEntity<List<MaintenanceBillResponse>> getAllBills() {
        return ResponseEntity.ok(billService.getAllBills());
    }

    @GetMapping("/admin/maintenance/defaulters")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARY')")
    public ResponseEntity<List<MaintenanceBillResponse>> getDefaulters() {
        return ResponseEntity.ok(billService.getDefaulters());
    }

    @PutMapping("/admin/maintenance/bills/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARY')")
    public ResponseEntity<MaintenanceBillResponse> updateBill(
            @PathVariable Long id,
            @RequestBody BillRequest request) {
        return ResponseEntity.ok(billService.updateBill(id, request));
    }

    @PutMapping("/admin/maintenance/bills/{id}/mark-paid")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARY')")
    public ResponseEntity<MaintenanceBillResponse> markPaid(@PathVariable Long id) {
        return ResponseEntity.ok(billService.markAsPaid(id));
    }

    // --- Resident APIs ---

    @GetMapping("/resident/maintenance/my-bills")
    @PreAuthorize("hasAnyRole('RESIDENT', 'OWNER')")
    public ResponseEntity<List<MaintenanceBillResponse>> getMyBills() {
        return ResponseEntity.ok(billService.getMyBills());
    }

    @GetMapping("/resident/maintenance/history")
    @PreAuthorize("hasAnyRole('RESIDENT', 'OWNER')")
    public ResponseEntity<List<MaintenanceBillResponse>> getBillHistory() {
        return ResponseEntity.ok(billService.getMyBills());
    }

    @GetMapping("/resident/maintenance/bills/{id}")
    @PreAuthorize("hasAnyRole('RESIDENT', 'OWNER', 'ADMIN', 'SECRETARY')")
    public ResponseEntity<MaintenanceBillResponse> getBillDetails(@PathVariable Long id) {
        return ResponseEntity.ok(billService.getBillDetails(id));
    }

    // --- Dashboard API ---

    @GetMapping("/admin/maintenance/dashboard/stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARY')")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        return ResponseEntity.ok(billService.getDashboardStats());
    }
}
