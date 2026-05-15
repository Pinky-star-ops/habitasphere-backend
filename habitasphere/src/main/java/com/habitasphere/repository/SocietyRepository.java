package com.habitasphere.repository;

import com.habitasphere.entity.Society;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocietyRepository extends JpaRepository<Society, Long> {
}