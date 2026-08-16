package com.habitasphere.dto;

import com.habitasphere.enums.ParcelStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class ParcelResponse {

    private Long id;

    private String trackingNumber;

    private String courierName;

    private String senderName;

    private Long receiverId;

    private String receiverName;

    private LocalDate receivedDate;

    private LocalTime receivedTime;

    private ParcelStatus status;

    private String description;

    private LocalDate collectedDate;

    private LocalTime collectedTime;

    private Long societyId;

    private LocalDateTime createdAt;
}