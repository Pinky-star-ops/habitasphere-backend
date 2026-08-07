package com.habitasphere.dto;

import com.habitasphere.enums.BillStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceBillResponse {

    private Long id;
    private Integer month;
    private Integer year;
    private Double amount;
    private Double paidAmount;
    private Double dueAmount;
    private Double lateFee;
    private LocalDateTime generatedAt;
    private LocalDate dueDate;
    private BillStatus status;
    private Long residentId;
    private String residentName;
    private String residentEmail;
}
