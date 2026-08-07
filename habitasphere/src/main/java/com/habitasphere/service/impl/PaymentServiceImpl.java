package com.habitasphere.service.impl;

import com.habitasphere.dto.CollectionSummaryResponse;
import com.habitasphere.dto.PaymentRequest;
import com.habitasphere.dto.PaymentResponse;
import com.habitasphere.entity.MaintenanceBill;
import com.habitasphere.entity.Payment;
import com.habitasphere.entity.RoleType;
import com.habitasphere.entity.User;
import com.habitasphere.enums.BillStatus;
import com.habitasphere.enums.PaymentMethod;
import com.habitasphere.enums.PaymentStatus;
import com.habitasphere.exception.BadRequestException;
import com.habitasphere.exception.ResourceNotFoundException;
import com.habitasphere.repository.MaintenanceBillRepository;
import com.habitasphere.repository.PaymentRepository;
import com.habitasphere.repository.UserRepository;
import com.habitasphere.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final MaintenanceBillRepository billRepository;

    @Override
    @Transactional
    public PaymentResponse payBill(PaymentRequest request) {
        validatePaymentRequest(request);
        User resident = getAuthenticatedUser();

        MaintenanceBill bill = billRepository.findById(request.getBillId())
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance bill not found with ID: " + request.getBillId()));

        // Ownership validation
        if (bill.getResident() == null || !bill.getResident().getId().equals(resident.getId())) {
            throw new BadRequestException("You can only make payments for yourself.");
        }

        // Prevent invalid bill payments: already paid
        if (bill.getStatus() == BillStatus.PAID) {
            throw new BadRequestException("Bill is already paid.");
        }

        // Validate amount
        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new BadRequestException("Payment amount must be greater than zero.");
        }
        if (request.getAmount() > bill.getDueAmount()) {
            throw new BadRequestException("Payment amount cannot exceed the due amount of: " + bill.getDueAmount());
        }

        // Parse payment method
        PaymentMethod paymentMethod;
        try {
            paymentMethod = PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new BadRequestException("Invalid payment method: " + request.getPaymentMethod());
        }

        // Generate unique transactionId
        String transactionId = "TXN-" + UUID.randomUUID().toString().replace("-", "").toUpperCase().substring(0, 10);

        Payment payment = Payment.builder()
                .transactionId(transactionId)
                .amount(request.getAmount())
                .status(PaymentStatus.SUCCESS)
                .paymentMethod(paymentMethod)
                .paymentDate(LocalDateTime.now())
                .resident(resident)
                .bill(bill)
                .build();

        // Save payment and update bill status
        Payment savedPayment = paymentRepository.save(payment);
        
        bill.setPaidAmount(bill.getPaidAmount() + request.getAmount());
        bill.setDueAmount(bill.getAmount() + bill.getLateFee() - bill.getPaidAmount());
        
        if (bill.getDueAmount() <= 0) {
            bill.setStatus(BillStatus.PAID);
        } else {
            bill.setStatus(BillStatus.PARTIALLY_PAID);
        }
        billRepository.save(bill);

        return toResponse(savedPayment);
    }

    @Override
    public List<PaymentResponse> getMyPayments() {
        User resident = getAuthenticatedUser();

        return paymentRepository.findByResidentId(resident.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public PaymentResponse getPaymentDetails(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + id));

        User user = getAuthenticatedUser();

        boolean isAdminOrSecretary = user.getRoles().stream()
                .anyMatch(role -> role.getName() == RoleType.ROLE_ADMIN || role.getName() == RoleType.ROLE_SECRETARY);

        if (!isAdminOrSecretary && (payment.getResident() == null || !payment.getResident().getId().equals(user.getId()))) {
            throw new BadRequestException("You do not have permission to view this transaction.");
        }

        return toResponse(payment);
    }

    @Override
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CollectionSummaryResponse getCollectionSummary() {
        List<Payment> allPayments = paymentRepository.findAll();

        double totalAmountCollected = 0.0;
        long successfulCount = 0;
        long pendingCount = 0;
        long failedCount = 0;
        Map<String, Double> methodBreakdown = new HashMap<>();

        // Initialize methods to 0.0
        for (PaymentMethod method : PaymentMethod.values()) {
            methodBreakdown.put(method.name(), 0.0);
        }

        for (Payment payment : allPayments) {
            PaymentStatus status = payment.getStatus();
            if (status == PaymentStatus.SUCCESS) {
                successfulCount++;
                double amount = payment.getAmount() != null ? payment.getAmount() : 0.0;
                totalAmountCollected += amount;
                
                if (payment.getPaymentMethod() != null) {
                    String methodName = payment.getPaymentMethod().name();
                    methodBreakdown.put(methodName, methodBreakdown.getOrDefault(methodName, 0.0) + amount);
                }
            } else if (status == PaymentStatus.PENDING) {
                pendingCount++;
            } else if (status == PaymentStatus.FAILED || status == PaymentStatus.CANCELLED) {
                failedCount++;
            }
        }

        return CollectionSummaryResponse.builder()
                .totalAmountCollected(totalAmountCollected)
                .successfulPaymentsCount(successfulCount)
                .pendingPaymentsCount(pendingCount)
                .failedPaymentsCount(failedCount)
                .paymentMethodBreakdown(methodBreakdown)
                .build();
    }

    @Override
    @Transactional
    public PaymentResponse updatePaymentStatus(Long id, PaymentStatus status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + id));

        payment.setStatus(status);

        // If updated to SUCCESS, ensure bill is updated
        if (status == PaymentStatus.SUCCESS && payment.getBill() != null) {
            MaintenanceBill bill = payment.getBill();
            bill.setPaidAmount(bill.getPaidAmount() + payment.getAmount());
            bill.setDueAmount(bill.getAmount() + bill.getLateFee() - bill.getPaidAmount());
            if (bill.getDueAmount() <= 0) {
                bill.setStatus(BillStatus.PAID);
            } else {
                bill.setStatus(BillStatus.PARTIALLY_PAID);
            }
            billRepository.save(bill);
        }

        return toResponse(paymentRepository.save(payment));
    }

    private PaymentResponse toResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .transactionId(payment.getTransactionId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .paymentDate(payment.getPaymentDate())
                .paymentMethod(payment.getPaymentMethod() != null ? payment.getPaymentMethod().name() : null)
                .billId(payment.getBill() != null ? payment.getBill().getId() : null)
                .residentId(payment.getResident() != null ? payment.getResident().getId() : null)
                .build();
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new BadRequestException("Authenticated user is required.");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + authentication.getName()));
    }

    private void validatePaymentRequest(PaymentRequest request) {
        if (request == null) {
            throw new BadRequestException("Payment request is required.");
        }

        if (request.getBillId() == null) {
            throw new BadRequestException("Bill ID is required.");
        }

        if (request.getAmount() == null) {
            throw new BadRequestException("Payment amount is required.");
        }

        if (request.getPaymentMethod() == null || request.getPaymentMethod().isBlank()) {
            throw new BadRequestException("Payment method is required.");
        }
    }
}
