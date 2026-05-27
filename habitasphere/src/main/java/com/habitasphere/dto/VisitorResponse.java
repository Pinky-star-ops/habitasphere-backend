package com.habitasphere.dto;

import com.habitasphere.enums.VisitorStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisitorResponse {

    private Long id;

    private String visitorName;

    private String phoneNumber;

    private String purpose;

    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    private VisitorStatus status;

    private String apartmentNumber;

    private String createdBy;
}