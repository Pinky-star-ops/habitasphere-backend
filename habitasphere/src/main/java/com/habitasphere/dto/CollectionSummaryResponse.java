package com.habitasphere.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class CollectionSummaryResponse {

    private Double totalAmountCollected;

    private Long successfulPaymentsCount;

    private Long pendingPaymentsCount;

    private Long failedPaymentsCount;

    private Map<String, Double> paymentMethodBreakdown;
}
