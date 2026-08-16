package com.habitasphere.repository;

import com.habitasphere.entity.Vendor;
import com.habitasphere.enums.VendorServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

    List<Vendor> findByServiceType(VendorServiceType serviceType);

    List<Vendor> findByIsActive(Boolean isActive);

    List<Vendor> findBySocietyId(Long societyId);

    List<Vendor> findBySocietyIdAndServiceType(
            Long societyId,
            VendorServiceType serviceType
    );

    long countByIsActive(Boolean isActive);

    long countBySocietyId(Long societyId);

    long countBySocietyIdAndIsActive(
            Long societyId,
            Boolean isActive
    );
}