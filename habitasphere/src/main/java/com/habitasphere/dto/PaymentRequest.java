package com.habitasphere.dto;

import lombok.Data;

@Data
public class PaymentRequest {

    private Long billId;

    private Double amount;

    private String paymentMethod;
}