package com.habitasphere.repository;

import com.habitasphere.entity.Payment;
import com.habitasphere.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByResidentId(Long residentId);

    List<Payment> findByStatus(PaymentStatus status);

    long countByStatus(PaymentStatus status);

    long countByResidentId(Long residentId);

    long countByResidentIdAndStatus(
            Long residentId,
            PaymentStatus status
    );

    @Query("""
            SELECT COALESCE(SUM(p.amount),0)
            FROM Payment p
            """)
    Double getTotalRevenue();
}