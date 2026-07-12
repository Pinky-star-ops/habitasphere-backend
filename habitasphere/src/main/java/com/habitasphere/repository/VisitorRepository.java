package com.habitasphere.repository;

import com.habitasphere.entity.Visitor;
import com.habitasphere.enums.VisitorStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    List<Visitor> findByApartmentId(Long apartmentId);

    long countByStatus(VisitorStatus status);

    long countByCreatedById(Long userId);

    long countByCreatedByIdAndStatus(
            Long userId,
            VisitorStatus status
    );
}