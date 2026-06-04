package com.habitasphere.dto;

import com.habitasphere.enums.BillStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MaintenanceBillResponse {

    private Long id;
    private String billMonth;
    private Double amount;
    private LocalDate generatedDate;
    private LocalDate dueDate;
    private BillStatus status;
    private Long residentId;
    private String residentName;
    private String residentEmail;
}
