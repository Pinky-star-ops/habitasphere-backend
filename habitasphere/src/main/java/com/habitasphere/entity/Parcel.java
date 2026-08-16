package com.habitasphere.entity;

import com.habitasphere.enums.ParcelStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(
    name = "parcels",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "tracking_number")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parcel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tracking_number", nullable = false, unique = true)
    private String trackingNumber;

    private String courierName;

    private String senderName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    private LocalDate receivedDate;

    private LocalTime receivedTime;

    @Enumerated(EnumType.STRING)
    private ParcelStatus status;

    private String description;

    private LocalDate collectedDate;

    private LocalTime collectedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_id", nullable = false)
    private Society society;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();

        if (receivedDate == null) {
            receivedDate = LocalDate.now();
        }

        if (receivedTime == null) {
            receivedTime = LocalTime.now();
        }

        if (status == null) {
            status = ParcelStatus.RECEIVED;
        }
    }
}