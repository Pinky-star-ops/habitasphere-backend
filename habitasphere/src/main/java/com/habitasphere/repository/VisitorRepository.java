package com.habitasphere.repository;

import com.habitasphere.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    List<Visitor> findByApartmentId(Long apartmentId);
}
