package com.habitasphere.repository;

import com.habitasphere.entity.RoleType;
import com.habitasphere.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    @Query("""
SELECT COUNT(DISTINCT u)
FROM User u
JOIN u.roles r
WHERE r.name = :role
""")
long countUsersByRole(@Param("role") RoleType role);
    boolean existsByApartmentId(Long apartmentId);
}
