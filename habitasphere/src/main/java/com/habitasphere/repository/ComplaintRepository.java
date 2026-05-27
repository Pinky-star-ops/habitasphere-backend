package com.habitasphere.repository;

import com.habitasphere.entity.Complaint;
import com.habitasphere.entity.User;
import com.habitasphere.enums.ComplaintStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    List<Complaint> findByResident(User resident);

    Page<Complaint> findByStatus(ComplaintStatus status, Pageable pageable);
}