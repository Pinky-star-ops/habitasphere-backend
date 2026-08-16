package com.habitasphere.service.impl;

import com.habitasphere.dto.ParcelRequest;
import com.habitasphere.dto.ParcelResponse;
import com.habitasphere.entity.Parcel;
import com.habitasphere.entity.User;
import com.habitasphere.enums.ParcelStatus;
import com.habitasphere.repository.ParcelRepository;
import com.habitasphere.repository.UserRepository;
import com.habitasphere.repository.SocietyRepository;
import com.habitasphere.service.ParcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParcelServiceImpl implements ParcelService {

    private final ParcelRepository parcelRepository;
    private final UserRepository userRepository;
    

    @Override
public ParcelResponse createParcel(ParcelRequest request, String username) {

    if (parcelRepository.existsByTrackingNumber(request.getTrackingNumber())) {
        throw new RuntimeException("Tracking number already exists");
    }

    User receiver = userRepository.findById(request.getReceiverId())
            .orElseThrow(() -> new RuntimeException("Receiver not found"));

    if (receiver.getSociety() == null) {
        throw new RuntimeException("Receiver is not assigned to a society");
    }

    Parcel parcel = new Parcel();

    parcel.setTrackingNumber(request.getTrackingNumber());
    parcel.setCourierName(request.getCourierName());
    parcel.setSenderName(request.getSenderName());
    parcel.setReceiver(receiver);
    parcel.setSociety(receiver.getSociety());
    parcel.setDescription(request.getDescription());

    parcel.setReceivedDate(LocalDate.now());
    parcel.setReceivedTime(LocalTime.now());
    parcel.setStatus(ParcelStatus.RECEIVED);

    Parcel savedParcel = parcelRepository.save(parcel);

    return convertToResponse(savedParcel);
}

    @Override
    public List<ParcelResponse> getAllParcels(String username) {

        return parcelRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ParcelResponse getParcelById(Long id, String username) {

        Parcel parcel = parcelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parcel not found"));

        return convertToResponse(parcel);
    }

    @Override
    public ParcelResponse updateParcel(
            Long id,
            ParcelRequest request,
            String username) {

        Parcel parcel = parcelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parcel not found"));

        if (!parcel.getTrackingNumber().equals(request.getTrackingNumber())
                && parcelRepository.existsByTrackingNumber(request.getTrackingNumber())) {

            throw new RuntimeException("Tracking number already exists");
        }

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        parcel.setTrackingNumber(request.getTrackingNumber());
        parcel.setCourierName(request.getCourierName());
        parcel.setSenderName(request.getSenderName());
        parcel.setReceiver(receiver);
        parcel.setDescription(request.getDescription());

        Parcel updatedParcel = parcelRepository.save(parcel);

        return convertToResponse(updatedParcel);
    }

    @Override
    public ParcelResponse collectParcel(Long id, String username) {

        Parcel parcel = parcelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parcel not found"));

        if (parcel.getStatus() != ParcelStatus.RECEIVED
                && parcel.getStatus() != ParcelStatus.NOTIFIED) {

            throw new RuntimeException(
                    "Parcel can only be collected when it is received or notified"
            );
        }

        parcel.setStatus(ParcelStatus.COLLECTED);
        parcel.setCollectedDate(LocalDate.now());
        parcel.setCollectedTime(LocalTime.now());

        Parcel updatedParcel = parcelRepository.save(parcel);

        return convertToResponse(updatedParcel);
    }

    @Override
    public ParcelResponse returnParcel(Long id, String username) {

        Parcel parcel = parcelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parcel not found"));

        if (parcel.getStatus() == ParcelStatus.COLLECTED) {
            throw new RuntimeException(
                    "Collected parcel cannot be returned"
            );
        }

        parcel.setStatus(ParcelStatus.RETURNED);

        Parcel updatedParcel = parcelRepository.save(parcel);

        return convertToResponse(updatedParcel);
    }

    @Override
    public List<ParcelResponse> getMyParcels(String username) {

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return parcelRepository.findByReceiverId(user.getId())
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private ParcelResponse convertToResponse(Parcel parcel) {

        return ParcelResponse.builder()
                .id(parcel.getId())
                .trackingNumber(parcel.getTrackingNumber())
                .courierName(parcel.getCourierName())
                .senderName(parcel.getSenderName())
                .receiverId(parcel.getReceiver().getId())
                .receiverName(parcel.getReceiver().getName())
                .receivedDate(parcel.getReceivedDate())
                .receivedTime(parcel.getReceivedTime())
                .status(parcel.getStatus())
                .description(parcel.getDescription())
                .collectedDate(parcel.getCollectedDate())
                .collectedTime(parcel.getCollectedTime())
                .societyId(
                        parcel.getSociety() != null
                                ? parcel.getSociety().getId()
                                : null
                )
                .createdAt(parcel.getCreatedAt())
                .build();
    }
}