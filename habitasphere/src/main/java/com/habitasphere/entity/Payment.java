package com.habitasphere.entity;

import com.habitasphere.enums.PaymentMethod;
import com.habitasphere.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private LocalDateTime paymentDate;

    @ManyToOne
    @JoinColumn(name = "resident_id")
    private User resident;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private MaintenanceBill bill;
}