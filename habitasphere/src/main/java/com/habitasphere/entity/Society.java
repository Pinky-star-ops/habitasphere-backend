package com.habitasphere.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "societies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Society {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String address;
    private String city;
    private String state;
    private String pincode;

    @OneToMany(mappedBy = "society", cascade = CascadeType.ALL)
    private List<Apartment> apartments;

    @OneToMany(mappedBy = "society", cascade = CascadeType.ALL)
    private List<User> users;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}