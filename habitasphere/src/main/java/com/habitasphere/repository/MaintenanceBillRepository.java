package com.habitasphere.repository;

import com.habitasphere.entity.MaintenanceBill;
import com.habitasphere.entity.User;
import com.habitasphere.enums.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceBillRepository
        extends JpaRepository<MaintenanceBill, Long> {

    List<MaintenanceBill> findByResident(User resident);

    List<MaintenanceBill> findByStatus(BillStatus status);

}