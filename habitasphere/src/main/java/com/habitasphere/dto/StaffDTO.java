package com.habitasphere.dto;

import com.habitasphere.enums.StaffType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StaffDTO {

    private Long id;

    private String name;

    private String phone;

    private String email;

    private StaffType staffType;

    private LocalDate joiningDate;

    private Double salary;

    private String address;

    private Boolean isActive;

    private Long societyId;
}