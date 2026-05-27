package com.habitasphere.service;

import com.habitasphere.dto.VisitorRequest;
import com.habitasphere.dto.VisitorResponse;
import com.habitasphere.entity.Apartment;
import com.habitasphere.entity.User;
import com.habitasphere.entity.Visitor;
import com.habitasphere.enums.VisitorStatus;
import com.habitasphere.exception.ResourceNotFoundException;
import com.habitasphere.repository.ApartmentRepository;
import com.habitasphere.repository.UserRepository;
import com.habitasphere.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitorService {

    private final VisitorRepository visitorRepository;
    private final ApartmentRepository apartmentRepository;
    private final UserRepository userRepository;

    // CREATE VISITOR ENTRY
    public VisitorResponse createVisitor(VisitorRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User securityGuard = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Apartment apartment = apartmentRepository.findById(request.getApartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Apartment not found"));

        Visitor visitor = new Visitor();

        visitor.setVisitorName(request.getVisitorName());
        visitor.setPhoneNumber(request.getPhoneNumber());
        visitor.setPurpose(request.getPurpose());

        visitor.setApartment(apartment);
        visitor.setCreatedBy(securityGuard);

        visitor.setEntryTime(LocalDateTime.now());
        visitor.setStatus(VisitorStatus.PENDING);

        Visitor savedVisitor = visitorRepository.save(visitor);

        return mapToResponse(savedVisitor);
    }

    // APPROVE VISITOR
    public VisitorResponse approveVisitor(Long visitorId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User resident = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Visitor visitor = visitorRepository.findById(visitorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Visitor not found"));

        // OWNERSHIP VALIDATION
        if (!visitor.getApartment().getId()
                .equals(resident.getApartment().getId())) {

            throw new AccessDeniedException(
                    "You cannot approve visitors for another apartment");
        }

        visitor.setStatus(VisitorStatus.APPROVED);

        Visitor updatedVisitor = visitorRepository.save(visitor);

        return mapToResponse(updatedVisitor);
    }

    // REJECT VISITOR
    public VisitorResponse rejectVisitor(Long visitorId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User resident = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Visitor visitor = visitorRepository.findById(visitorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Visitor not found"));

        // OWNERSHIP VALIDATION
        if (!visitor.getApartment().getId()
                .equals(resident.getApartment().getId())) {

            throw new AccessDeniedException(
                    "You cannot reject visitors for another apartment");
        }

        visitor.setStatus(VisitorStatus.REJECTED);

        Visitor updatedVisitor = visitorRepository.save(visitor);

        return mapToResponse(updatedVisitor);
    }

    // MARK EXIT
    public VisitorResponse markExit(Long visitorId) {

        Visitor visitor = visitorRepository.findById(visitorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Visitor not found"));

        visitor.setExitTime(LocalDateTime.now());
        visitor.setStatus(VisitorStatus.EXITED);

        Visitor updatedVisitor = visitorRepository.save(visitor);

        return mapToResponse(updatedVisitor);
    }

    // ADMIN - GET ALL VISITORS
    public List<VisitorResponse> getAllVisitors() {

        return visitorRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // RESIDENT - GET MY VISITORS
    public List<VisitorResponse> getMyVisitors() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User resident = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        List<Visitor> visitors =
                visitorRepository.findByApartmentId(
                        resident.getApartment().getId());

        return visitors.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // DTO MAPPING
    private VisitorResponse mapToResponse(Visitor visitor) {

        VisitorResponse response = new VisitorResponse();

        response.setId(visitor.getId());
        response.setVisitorName(visitor.getVisitorName());
        response.setPhoneNumber(visitor.getPhoneNumber());
        response.setPurpose(visitor.getPurpose());

        response.setEntryTime(visitor.getEntryTime());
        response.setExitTime(visitor.getExitTime());

        response.setStatus(visitor.getStatus());

        response.setApartmentNumber(
                visitor.getApartment().getApartmentNumber());

        response.setCreatedBy(
                visitor.getCreatedBy().getName());

        return response;
    }
}
