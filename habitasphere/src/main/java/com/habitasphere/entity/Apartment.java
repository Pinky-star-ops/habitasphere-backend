package com.habitasphere.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "apartments")
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String apartmentNumber;

    private String blockName;

    private Integer floor;

    // MANY apartments belong to ONE society
    @ManyToOne
    @JoinColumn(name = "society_id")
    private Society society;

    public Apartment() {
    }

    public Apartment(Long id, String apartmentNumber, String blockName, Integer floor, Society society) {
        this.id = id;
        this.apartmentNumber = apartmentNumber;
        this.blockName = blockName;
        this.floor = floor;
        this.society = society;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }
}