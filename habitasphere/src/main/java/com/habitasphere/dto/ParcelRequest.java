package com.habitasphere.dto;

import lombok.Data;

@Data
public class ParcelRequest {

    private String trackingNumber;

    private String courierName;

    private String senderName;

    private Long receiverId;

    private String description;
}