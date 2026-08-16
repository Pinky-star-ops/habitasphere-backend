package com.habitasphere.dto;

import com.habitasphere.enums.VendorServiceType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VendorDTO {

    private Long id;

    private String name;

    private String companyName;

    private String phone;

    private String email;

    private VendorServiceType serviceType;

    private String address;

    private LocalDate contractStartDate;

    private LocalDate contractEndDate;

    private Boolean isActive;

    private Long societyId;
}