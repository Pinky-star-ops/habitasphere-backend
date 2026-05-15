package com.habitasphere.service;
import com.habitasphere.entity.Apartment;
import com.habitasphere.repository.ApartmentRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class ApartmentService {
    private final ApartmentRepository apartmentRepository;
    public Apartment createApartment(Apartment apartment){
        return apartmentRepository.save(apartment);
    }
    public List<Apartment> getAllApartments(){
        return apartmentRepository.findAll();
    }
}
