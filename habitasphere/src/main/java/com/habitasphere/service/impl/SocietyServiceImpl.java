package com.habitasphere.service.impl;

import com.habitasphere.dto.SocietyRequest;
import com.habitasphere.dto.SocietyResponse;
import com.habitasphere.entity.Society;
import com.habitasphere.exception.ResourceNotFoundException;
import com.habitasphere.repository.SocietyRepository;
import com.habitasphere.service.SocietyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SocietyServiceImpl implements SocietyService {

    private final SocietyRepository societyRepository;

    @Override
    public SocietyResponse createSociety(SocietyRequest request) {

        Society society = Society.builder()
                .name(request.getName())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .pinCode(request.getPinCode())
                .build();

        Society savedSociety = societyRepository.save(society);

        return mapToResponse(savedSociety);
    }

    @Override
    public List<SocietyResponse> getAllSocieties() {

        return societyRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SocietyResponse getSocietyById(Long id) {

        Society society = societyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Society not found with ID: " + id));

        return mapToResponse(society);
    }

    @Override
    public SocietyResponse updateSociety(Long id, SocietyRequest request) {

        Society society = societyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Society not found with ID: " + id));

        society.setName(request.getName());
        society.setAddress(request.getAddress());
        society.setCity(request.getCity());
        society.setState(request.getState());
        society.setCountry(request.getCountry());
        society.setPinCode(request.getPinCode());

        Society updatedSociety = societyRepository.save(society);

        return mapToResponse(updatedSociety);
    }

    @Override
    public void deleteSociety(Long id) {

        Society society = societyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Society not found with ID: " + id));

        societyRepository.delete(society);
    }

    private SocietyResponse mapToResponse(Society society) {

        return SocietyResponse.builder()
                .id(society.getId())
                .name(society.getName())
                .address(society.getAddress())
                .city(society.getCity())
                .state(society.getState())
                .country(society.getCountry())
                .pinCode(society.getPinCode())
                .build();
    }
}