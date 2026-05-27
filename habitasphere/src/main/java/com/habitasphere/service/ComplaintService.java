package com.habitasphere.service;

import com.habitasphere.dto.ComplaintRequestDto;
import com.habitasphere.dto.ComplaintResponseDto;
import com.habitasphere.enums.ComplaintStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ComplaintService {

    ComplaintResponseDto createComplaint(
            ComplaintRequestDto dto,
            String email
    );

    List<ComplaintResponseDto> getMyComplaints(
            String email
    );

    Page<ComplaintResponseDto> getAllComplaints(
            int page,
            int size
    );

    ComplaintResponseDto updateStatus(
            Long id,
            ComplaintStatus status
    );
}