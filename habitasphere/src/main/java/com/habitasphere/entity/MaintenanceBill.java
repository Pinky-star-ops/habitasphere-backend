package com.habitasphere.entity;

import com.habitasphere.enums.BillStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "maintenance_bills",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "resident_id",
                                "bill_month",
                                "bill_year"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bill_month", nullable = false)
    private Integer month;

    @Column(name = "bill_year", nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    @Builder.Default
    private Double paidAmount = 0.0;

    @Column(nullable = false)
    @Builder.Default
    private Double dueAmount = 0.0;

    @Column(nullable = false)
    @Builder.Default
    private Double lateFee = 0.0;

    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDateTime generatedAt;

    @Enumerated(EnumType.STRING)
    private BillStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id", nullable = false)
    private User resident;

    @PrePersist
    public void prePersist() {
        if (generatedAt == null) {
            generatedAt = LocalDateTime.now();
        }

        if (paidAmount == null) {
            paidAmount = 0.0;
        }

        if (lateFee == null) {
            lateFee = 0.0;
        }

        if (dueAmount == null) {
            dueAmount = (amount != null ? amount : 0.0) + lateFee - paidAmount;
        }
    }
}