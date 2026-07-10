package com.habitasphere.service;

import com.habitasphere.dto.FacilityBookingRequest;
import com.habitasphere.dto.FacilityBookingResponse;
import com.habitasphere.entity.Facility;
import com.habitasphere.entity.FacilityBooking;
import com.habitasphere.entity.User;
import com.habitasphere.enums.BookingStatus;
import com.habitasphere.exception.BadRequestException;
import com.habitasphere.exception.ResourceNotFoundException;
import com.habitasphere.repository.FacilityBookingRepository;
import com.habitasphere.repository.FacilityRepository;
import com.habitasphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacilityBookingService {

    private final FacilityBookingRepository facilityBookingRepository;
    private final FacilityRepository facilityRepository;
    private final UserRepository userRepository;

    public FacilityBookingResponse bookFacility(FacilityBookingRequest request) {
        // Validation: Purpose cannot be blank
        if (request.getPurpose() == null || request.getPurpose().trim().isEmpty()) {
            throw new BadRequestException("Booking purpose cannot be blank");
        }

        // Validation: Booking date today or future
        LocalDate today = LocalDate.now();
        if (request.getBookingDate().isBefore(today)) {
            throw new BadRequestException("Booking date cannot be in the past");
        }

        // Validation: End time > Start time
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new BadRequestException("End time must be after start time");
        }

        // Validation: Facility exists and is active
        Facility facility = facilityRepository.findById(request.getFacilityId())
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found with ID: " + request.getFacilityId()));
        if (!facility.isActive()) {
            throw new BadRequestException("Facility is inactive and cannot be booked");
        }

        // Validation: Check for overlapping bookings
        // Only APPROVED and PENDING block slots
        boolean isOverlapping = facilityBookingRepository
                .existsByFacilityIdAndBookingDateAndStatusInAndStartTimeLessThanAndEndTimeGreaterThan(
                        request.getFacilityId(),
                        request.getBookingDate(),
                        List.of(BookingStatus.PENDING, BookingStatus.APPROVED),
                        request.getEndTime(),
                        request.getStartTime()
                );
        if (isOverlapping) {
            throw new BadRequestException("The facility is already booked/reserved for the requested time slot");
        }

        // Get currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        // Create booking with status PENDING
        FacilityBooking booking = FacilityBooking.builder()
                .facility(facility)
                .user(user)
                .bookingDate(request.getBookingDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .purpose(request.getPurpose().trim())
                .status(BookingStatus.PENDING)
                .build();

        FacilityBooking saved = facilityBookingRepository.save(booking);
        return mapToResponse(saved);
    }

    public void cancelBooking(Long id) {
        FacilityBooking booking = facilityBookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));

        // Get currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Security check: Only owner of the booking can cancel it
        if (!booking.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("You do not have permission to cancel this booking");
        }

        // Validation: Cannot cancel APPROVED booking if booking time has already passed
        if (booking.getStatus() == BookingStatus.APPROVED) {
            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();
            if (today.isAfter(booking.getBookingDate()) ||
                    (today.isEqual(booking.getBookingDate()) && now.isAfter(booking.getStartTime()))) {
                throw new BadRequestException("Cannot cancel this booking because the booking time has already passed");
            }
        }

        booking.setStatus(BookingStatus.CANCELLED);
        facilityBookingRepository.save(booking);
    }

    public List<FacilityBookingResponse> getMyBookings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return facilityBookingRepository.findByUserEmail(email).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<FacilityBookingResponse> getAllBookings() {
        return facilityBookingRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public FacilityBookingResponse approveBooking(Long id) {
        FacilityBooking booking = facilityBookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));

        // Reject duplicate approval/rejection
        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new BadRequestException("Booking is already approved");
        }
        if (booking.getStatus() == BookingStatus.REJECTED) {
            throw new BadRequestException("Booking is already rejected");
        }
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Booking is cancelled and cannot be approved");
        }

        booking.setStatus(BookingStatus.APPROVED);
        FacilityBooking saved = facilityBookingRepository.save(booking);
        return mapToResponse(saved);
    }

    public FacilityBookingResponse rejectBooking(Long id) {
        FacilityBooking booking = facilityBookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));

        // Reject duplicate approval/rejection
        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new BadRequestException("Booking is already approved");
        }
        if (booking.getStatus() == BookingStatus.REJECTED) {
            throw new BadRequestException("Booking is already rejected");
        }
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Booking is cancelled and cannot be rejected");
        }

        booking.setStatus(BookingStatus.REJECTED);
        FacilityBooking saved = facilityBookingRepository.save(booking);
        return mapToResponse(saved);
    }

    private FacilityBookingResponse mapToResponse(FacilityBooking booking) {
        return FacilityBookingResponse.builder()
                .id(booking.getId())
                .facilityId(booking.getFacility().getId())
                .facilityName(booking.getFacility().getName())
                .userId(booking.getUser().getId())
                .userName(booking.getUser().getName())
                .bookingDate(booking.getBookingDate())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .purpose(booking.getPurpose())
                .status(booking.getStatus())
                .build();
    }
}
