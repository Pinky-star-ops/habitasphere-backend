package com.habitasphere.controller;

import com.habitasphere.dto.CollectionSummaryResponse;
import com.habitasphere.dto.PaymentRequest;
import com.habitasphere.dto.PaymentResponse;
import com.habitasphere.enums.PaymentStatus;
import com.habitasphere.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/pay")
    @PreAuthorize("hasAuthority('ROLE_RESIDENT')")
    public PaymentResponse payBill(@RequestBody PaymentRequest request) {
        return paymentService.payBill(request);
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('ROLE_RESIDENT')")
    public List<PaymentResponse> getMyPayments() {
        return paymentService.getMyPayments();
    }

    @GetMapping("/summary")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SECRETARY')")
    public CollectionSummaryResponse getCollectionSummary() {
        return paymentService.getCollectionSummary();
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SECRETARY')")
    public List<PaymentResponse> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public PaymentResponse getPaymentDetails(@PathVariable Long id) {
        return paymentService.getPaymentDetails(id);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SECRETARY')")
    public PaymentResponse updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam PaymentStatus status
    ) {
        return paymentService.updatePaymentStatus(id, status);
    }
}
