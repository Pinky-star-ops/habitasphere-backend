package com.habitasphere.service;

import com.habitasphere.dto.SocietyRequest;
import com.habitasphere.dto.SocietyResponse;

import java.util.List;

public interface SocietyService {

    SocietyResponse createSociety(SocietyRequest request);

    List<SocietyResponse> getAllSocieties();

    SocietyResponse getSocietyById(Long id);

    SocietyResponse updateSociety(Long id, SocietyRequest request);

    void deleteSociety(Long id);
}