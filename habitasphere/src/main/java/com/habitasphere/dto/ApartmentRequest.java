package com.habitasphere.dto;

public class ApartmentRequest {
    private String apartmentNumber;
    private String blockName;
    private Integer floor;
    private Long SocietyId;

    public ApartmentRequest(){

    }

    public String getApartmentNumber(){
        return apartmentNumber;
    }
    public void setApartmentNumber(String apartmentNumber){
        this.apartmentNumber=apartmentNumber;

    }

    public String getBlockName(){
        return blockName;
    }

    public void setBlockName(String blockName){
        this.blockName=blockName;
    }

    public Integer getFloor(){
        return floor;
    }

    public void setFloor(Integer floor){
        this.floor=floor;
    }

    public Long getSocietyId(){
        return SocietyId;

    }

    public void setSocietyId(Long societyId) {
        this.SocietyId = societyId;
    }
}
