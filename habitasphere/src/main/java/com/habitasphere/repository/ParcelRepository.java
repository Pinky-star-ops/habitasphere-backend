package com.habitasphere.repository;

import com.habitasphere.entity.Parcel;
import com.habitasphere.enums.ParcelStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParcelRepository extends JpaRepository<Parcel, Long> {

    Optional<Parcel> findByTrackingNumber(String trackingNumber);

    boolean existsByTrackingNumber(String trackingNumber);

    List<Parcel> findByReceiverId(Long receiverId);

    List<Parcel> findBySocietyId(Long societyId);

    long countByStatusIn(List<ParcelStatus> statuses);

    long countByStatus(ParcelStatus status);
}