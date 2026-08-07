package com.habitasphere.repository;

import com.habitasphere.entity.MaintenanceBill;
import com.habitasphere.entity.User;
import com.habitasphere.enums.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MaintenanceBillRepository
        extends JpaRepository<MaintenanceBill, Long> {

    List<MaintenanceBill> findByResident(User resident);

    List<MaintenanceBill> findByResidentOrderByYearDescMonthDesc(User resident);

    List<MaintenanceBill> findByStatus(BillStatus status);

    List<MaintenanceBill> findByDueDateBeforeAndStatusNot(
            LocalDate date,
            BillStatus status
    );

    Optional<MaintenanceBill> findByResidentAndMonthAndYear(
            User resident,
            Integer month,
            Integer year
    );

    boolean existsByResidentAndMonthAndYear(
            User resident,
            Integer month,
            Integer year
    );

    @Query("""
            SELECT COALESCE(SUM(m.paidAmount), 0.0)
            FROM MaintenanceBill m
            """)
    Double getTotalCollected();

    @Query("""
            SELECT COALESCE(SUM(m.dueAmount), 0.0)
            FROM MaintenanceBill m
            WHERE m.status <> 'PAID'
            """)
    Double getPendingAmount();

    @Query("""
            SELECT COUNT(m)
            FROM MaintenanceBill m
            WHERE m.status = 'OVERDUE'
            """)
    Long getTotalDefaulters();

    @Query("""
            SELECT COALESCE(SUM(m.amount), 0.0)
            FROM MaintenanceBill m
            WHERE m.month = :month
            AND m.year = :year
            """)
    Double getMonthlyRevenue(@Param("month") Integer month, @Param("year") Integer year);

}