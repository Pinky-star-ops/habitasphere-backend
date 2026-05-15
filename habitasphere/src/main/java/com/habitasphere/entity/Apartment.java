package com.habitasphere.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "apartments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String block;

    private String number;

    private Integer floor;

    @ManyToOne
    @JoinColumn(name = "society_id")
    private Society society;

    @OneToMany(mappedBy = "apartment")
    private List<User> residents;
}