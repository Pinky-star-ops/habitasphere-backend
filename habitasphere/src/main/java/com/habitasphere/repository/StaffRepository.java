package com.habitasphere.repository;

import com.habitasphere.entity.Staff;
import com.habitasphere.enums.StaffType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    List<Staff> findByStaffType(StaffType staffType);

    List<Staff> findByIsActive(Boolean isActive);

    List<Staff> findBySocietyId(Long societyId);

    List<Staff> findBySocietyIdAndStaffType(
            Long societyId,
            StaffType staffType
    );

    long countByIsActive(Boolean isActive);

    long countBySocietyId(Long societyId);

    long countBySocietyIdAndIsActive(
            Long societyId,
            Boolean isActive
    );
}