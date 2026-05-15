package com.habitasphere.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SocietyRequest {

    @NotBlank(message = "Society name is required")
    private String name;

    private String address;

    private String city;

    private String state;

    private String country;

    private String pinCode;
}