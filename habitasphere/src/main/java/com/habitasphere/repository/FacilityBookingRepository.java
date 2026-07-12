package com.habitasphere.repository;

import com.habitasphere.entity.FacilityBooking;
import com.habitasphere.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface FacilityBookingRepository extends JpaRepository<FacilityBooking, Long> {

    List<FacilityBooking> findByUserEmail(String email);

    boolean existsByFacilityIdAndBookingDateAndStatusInAndStartTimeLessThanAndEndTimeGreaterThan(
            Long facilityId,
            LocalDate bookingDate,
            Collection<BookingStatus> statuses,
            LocalTime endTime,
            LocalTime startTime
    );

    long countByStatus(BookingStatus status);

    long countByUserId(Long userId);

    long countByUserIdAndStatus(
            Long userId,
            BookingStatus status
    );
}