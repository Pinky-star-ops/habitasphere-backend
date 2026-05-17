package com.habitasphere.controller;

import com.habitasphere.dto.ApartmentRequest;
import com.habitasphere.entity.Apartment;
import com.habitasphere.service.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/apartments")
public class ApartmentController {

    @Autowired
    private ApartmentService apartmentService;

    @PostMapping
    public Apartment createApartment(@RequestBody ApartmentRequest request) {
        return apartmentService.createApartment(request);
    }

    @GetMapping
    public List<Apartment> getAllApartments() {
        return apartmentService.getAllApartments();
    }
}