package com.habitasphere.dto;

import lombok.Data;

@Data
public class BillRequest {

    private Long residentId;

    private String billMonth;

    private Double amount;

    private String dueDate;

}