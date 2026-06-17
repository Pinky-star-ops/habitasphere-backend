package com.habitasphere.repository;

import com.habitasphere.entity.Payment;
import com.habitasphere.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByResidentId(Long residentId);

    List<Payment> findByStatus(PaymentStatus status);
}