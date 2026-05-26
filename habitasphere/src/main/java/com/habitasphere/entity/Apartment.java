package com.habitasphere.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "apartments")
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // old fields used by ApartmentService
    @Column(name = "apartment_number", nullable = false)
    private String apartmentNumber;

    private Integer floor;

    @Column(name = "block_name")
    private String blockName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_id")
    @JsonIgnore
    private Society society;

    // new resident relationship
    @OneToMany(mappedBy = "apartment")
    @JsonIgnore
    private List<User> residents;

    public Apartment() {
    }

    // ID
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // APARTMENT NUMBER
    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    // FLOOR
    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    // BLOCK NAME
    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    // SOCIETY
    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    // RESIDENTS
    public List<User> getResidents() {
        return residents;
    }

    public void setResidents(List<User> residents) {
        this.residents = residents;
    }
}
