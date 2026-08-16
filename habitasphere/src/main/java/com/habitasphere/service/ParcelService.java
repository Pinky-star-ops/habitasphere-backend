package com.habitasphere.service;

import com.habitasphere.dto.ParcelRequest;
import com.habitasphere.dto.ParcelResponse;

import java.util.List;

public interface ParcelService {

    ParcelResponse createParcel(ParcelRequest request, String username);

    List<ParcelResponse> getAllParcels(String username);

    ParcelResponse getParcelById(Long id, String username);

    ParcelResponse updateParcel(Long id, ParcelRequest request, String username);

    ParcelResponse collectParcel(Long id, String username);

    ParcelResponse returnParcel(Long id, String username);

    List<ParcelResponse> getMyParcels(String username);
}