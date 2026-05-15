package com.habitasphere.controller;
import com.habitasphere.entity.Apartment;
import com.habitasphere.service.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/apartments")
@RequiredArgsConstructor
public class ApartmentController {
    private final ApartmentService apartmentService;
    @PostMapping
    public ResponseEntity<Apartment> createApartment(@RequestBody Apartment apartment){
        return ResponseEntity.ok(apartmentService.createApartment(apartment));
    }
    @GetMapping
    public ResponseEntity<List<Apartment>> getAllApartments(){
        return ResponseEntity.ok(apartmentService.getAllApartments());
    }
}
