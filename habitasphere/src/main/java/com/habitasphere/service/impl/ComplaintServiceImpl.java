package com.habitasphere.service.impl;

import com.habitasphere.dto.ComplaintRequestDto;
import com.habitasphere.dto.ComplaintResponseDto;
import com.habitasphere.entity.Apartment;
import com.habitasphere.entity.Complaint;
import com.habitasphere.entity.User;
import com.habitasphere.enums.ComplaintStatus;
import com.habitasphere.repository.ApartmentRepository;
import com.habitasphere.repository.ComplaintRepository;
import com.habitasphere.repository.UserRepository;
import com.habitasphere.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;

    private final UserRepository userRepository;

    private final ApartmentRepository apartmentRepository;

    @Override
    public ComplaintResponseDto createComplaint(
            ComplaintRequestDto dto,
            String email
    ) {

        User resident = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Apartment apartment = apartmentRepository.findById(dto.getApartmentId())
                .orElseThrow(() ->
                        new RuntimeException("Apartment not found"));

        Complaint complaint = Complaint.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .priority(dto.getPriority())
                .status(ComplaintStatus.OPEN)
                .resident(resident)
                .apartment(apartment)
                .build();

        Complaint savedComplaint = complaintRepository.save(complaint);

        return mapToDto(savedComplaint);
    }

    @Override
    public List<ComplaintResponseDto> getMyComplaints(
            String email
    ) {

        User resident = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return complaintRepository.findByResident(resident)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public Page<ComplaintResponseDto> getAllComplaints(
            int page,
            int size
    ) {

        return complaintRepository.findAll(
                        PageRequest.of(page, size)
                )
                .map(this::mapToDto);
    }

    @Override
    public ComplaintResponseDto updateStatus(
            Long id,
            ComplaintStatus status
    ) {

        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Complaint not found"));

        validateStatusTransition(
                complaint.getStatus(),
                status
        );

        complaint.setStatus(status);

        if (status == ComplaintStatus.RESOLVED) {
            complaint.setResolvedAt(LocalDateTime.now());
        }

        Complaint updatedComplaint = complaintRepository.save(complaint);

        return mapToDto(updatedComplaint);
    }

    private void validateStatusTransition(
            ComplaintStatus currentStatus,
            ComplaintStatus newStatus
    ) {

        if (currentStatus == ComplaintStatus.OPEN
                && newStatus != ComplaintStatus.IN_PROGRESS) {

            throw new RuntimeException(
                    "OPEN complaints can only move to IN_PROGRESS"
            );
        }

        if (currentStatus == ComplaintStatus.IN_PROGRESS
                && newStatus != ComplaintStatus.RESOLVED) {

            throw new RuntimeException(
                    "IN_PROGRESS complaints can only move to RESOLVED"
            );
        }

        if (currentStatus == ComplaintStatus.RESOLVED
                && newStatus != ComplaintStatus.CLOSED) {

            throw new RuntimeException(
                    "RESOLVED complaints can only move to CLOSED"
            );
        }

        if (currentStatus == ComplaintStatus.CLOSED) {

            throw new RuntimeException(
                    "CLOSED complaints cannot be modified"
            );
        }
    }

    private ComplaintResponseDto mapToDto(
            Complaint complaint
    ) {

        return ComplaintResponseDto.builder()
                .id(complaint.getId())
                .title(complaint.getTitle())
                .description(complaint.getDescription())
                .status(complaint.getStatus())
                .priority(complaint.getPriority())
                .createdAt(complaint.getCreatedAt())
                .resolvedAt(complaint.getResolvedAt())
                .residentName(complaint.getResident().getName())
                .apartmentNumber(complaint.getApartment().getApartmentNumber())
                .build();
    }
}