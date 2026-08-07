package com.habitasphere.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillRequest {

    private Long residentId;

    private Integer month;

    private Integer year;

    private Double amount;

    private String dueDate;

}