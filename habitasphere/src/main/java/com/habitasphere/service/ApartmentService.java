package com.habitasphere.service;

import com.habitasphere.dto.ApartmentRequest;
import com.habitasphere.entity.Apartment;
import com.habitasphere.entity.Society;
import com.habitasphere.exception.BadRequestException;
import com.habitasphere.exception.ResourceNotFoundException;
import com.habitasphere.repository.ApartmentRepository;
import com.habitasphere.repository.SocietyRepository;
import com.habitasphere.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApartmentService {

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private SocietyRepository societyRepository;

    @Autowired
    private UserRepository userRepository;

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

    public void deleteApartment(Long id) {
        Apartment apartment = apartmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Apartment not found with ID: " + id));

        if (userRepository.existsByApartmentId(id)) {
            throw new BadRequestException("Cannot delete apartment because residents are assigned to it");
        }

        apartmentRepository.delete(apartment);
    }
}
