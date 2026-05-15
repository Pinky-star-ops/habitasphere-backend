package com.habitasphere.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SocietyResponse {

    private Long id;

    private String name;

    private String address;

    private String city;

    private String state;

    private String country;

    private String pinCode;
}