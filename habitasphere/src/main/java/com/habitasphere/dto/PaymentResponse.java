package com.habitasphere.dto;

import com.habitasphere.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponse {

    private Long id;

    private String transactionId;

    private Double amount;

    private PaymentStatus status;

    private LocalDateTime paymentDate;

    private String paymentMethod;

    private Long billId;

    private Long residentId;
}
