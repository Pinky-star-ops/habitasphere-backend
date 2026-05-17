package com.habitasphere.service;

import com.habitasphere.dto.ApartmentRequest;
import com.habitasphere.entity.Apartment;
import com.habitasphere.entity.Society;
import com.habitasphere.repository.ApartmentRepository;
import com.habitasphere.repository.SocietyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApartmentService {

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private SocietyRepository societyRepository;

    public Apartment createApartment(ApartmentRequest request) {

        Society society = societyRepository.findById(request.getSocietyId())
                .orElseThrow(() -> new RuntimeException("Society not found"));

        Apartment apartment = new Apartment();

        apartment.setApartmentNumber(request.getApartmentNumber());
        apartment.setBlockName(request.getBlockName());
        apartment.setFloor(request.getFloor());

        apartment.setSociety(society);

        return apartmentRepository.save(apartment);
    }

    public List<Apartment> getAllApartments() {
        return apartmentRepository.findAll();
    }
}