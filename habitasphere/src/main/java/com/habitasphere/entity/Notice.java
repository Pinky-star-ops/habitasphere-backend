package com.habitasphere.entity;

import com.habitasphere.enums.NoticePriority;
import com.habitasphere.enums.NoticeType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "notices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private NoticeType type;

    @Enumerated(EnumType.STRING)
    private NoticePriority priority;

    private LocalDateTime createdAt;

    private LocalDate expiryDate;

    private boolean active = true;
    private boolean pinned = false;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
}