package com.habitasphere.dto;

import com.habitasphere.enums.ComplaintStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateComplaintStatusDto {

    @NotNull(message = "Status is required")
    private ComplaintStatus status;
}