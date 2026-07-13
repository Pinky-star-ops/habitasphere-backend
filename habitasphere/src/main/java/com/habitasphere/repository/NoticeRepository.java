package com.habitasphere.repository;

import com.habitasphere.entity.Notice;
import com.habitasphere.enums.NoticePriority;
import com.habitasphere.enums.NoticeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("""
            SELECT n
            FROM Notice n
            WHERE n.active = true
            AND (
                    n.expiryDate IS NULL
                    OR n.expiryDate >= :today
                )
            ORDER BY n.pinned DESC,
         n.priority DESC,
         n.createdAt DESC
            """)
    List<Notice> findActiveNotices(
            @Param("today") LocalDate today
    );

    List<Notice> findByType(NoticeType type);

    List<Notice> findByPriority(NoticePriority priority);
}