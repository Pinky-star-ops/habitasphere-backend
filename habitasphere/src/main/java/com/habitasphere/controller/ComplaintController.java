package com.habitasphere.controller;

import com.habitasphere.dto.ComplaintRequestDto;
import com.habitasphere.dto.ComplaintResponseDto;
import com.habitasphere.dto.UpdateComplaintStatusDto;
import com.habitasphere.service.ComplaintService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;

    @PostMapping
    public ComplaintResponseDto createComplaint(
            @Valid @RequestBody ComplaintRequestDto dto,
            Authentication authentication
    ) {

        return complaintService.createComplaint(
                dto,
                authentication.getName()
        );
    }

    @GetMapping("/my")
    public List<ComplaintResponseDto> getMyComplaints(
            Authentication authentication
    ) {

        return complaintService.getMyComplaints(
                authentication.getName()
        );
    }

    @GetMapping
    public Page<ComplaintResponseDto> getAllComplaints(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return complaintService.getAllComplaints(
                page,
                size
        );
    }

    @PutMapping("/{id}/status")
    public ComplaintResponseDto updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateComplaintStatusDto dto
    ) {

        return complaintService.updateStatus(
                id,
                dto.getStatus()
        );
    }
}