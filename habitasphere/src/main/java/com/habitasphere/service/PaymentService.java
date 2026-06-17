package com.habitasphere.service;

import com.habitasphere.dto.CollectionSummaryResponse;
import com.habitasphere.dto.PaymentRequest;
import com.habitasphere.dto.PaymentResponse;
import com.habitasphere.enums.PaymentStatus;

import java.util.List;

public interface PaymentService {

    PaymentResponse payBill(PaymentRequest request);

    List<PaymentResponse> getMyPayments();

    PaymentResponse getPaymentDetails(Long id);

    List<PaymentResponse> getAllPayments();

    CollectionSummaryResponse getCollectionSummary();

    PaymentResponse updatePaymentStatus(Long id, PaymentStatus status);
}
