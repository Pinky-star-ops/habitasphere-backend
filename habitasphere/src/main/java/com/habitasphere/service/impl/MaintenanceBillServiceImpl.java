package com.habitasphere.service.impl;

import com.habitasphere.dto.BillRequest;
import com.habitasphere.dto.MaintenanceBillResponse;
import com.habitasphere.entity.MaintenanceBill;
import com.habitasphere.entity.User;
import com.habitasphere.enums.BillStatus;
import com.habitasphere.repository.MaintenanceBillRepository;
import com.habitasphere.repository.UserRepository;
import com.habitasphere.service.MaintenanceBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceBillServiceImpl
        implements MaintenanceBillService {

    private final MaintenanceBillRepository billRepository;
    private final UserRepository userRepository;

    @Override
    public MaintenanceBillResponse generateBill(BillRequest request) {

        User resident = userRepository.findById(
                request.getResidentId()
        ).orElseThrow(() ->
                new RuntimeException("Resident not found"));

        MaintenanceBill bill = MaintenanceBill.builder()
                .billMonth(request.getBillMonth())
                .amount(request.getAmount())
                .generatedDate(LocalDate.now())
                .dueDate(LocalDate.parse(request.getDueDate()))
                .status(BillStatus.UNPAID)
                .resident(resident)
                .build();

        return toResponse(billRepository.save(bill));
    }

    @Override
    public List<MaintenanceBillResponse> getAllBills() {
        return billRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<MaintenanceBillResponse> getBillsByResident(Long residentId) {

        User resident = userRepository.findById(
                residentId
        ).orElseThrow(() ->
                new RuntimeException("Resident not found"));

        return billRepository.findByResident(resident)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public MaintenanceBillResponse markAsPaid(Long billId) {

        MaintenanceBill bill = billRepository.findById(
                billId
        ).orElseThrow(() ->
                new RuntimeException("Bill not found"));

        bill.setStatus(BillStatus.PAID);

        return toResponse(billRepository.save(bill));
    }

    private MaintenanceBillResponse toResponse(MaintenanceBill bill) {
        User resident = bill.getResident();

        return MaintenanceBillResponse.builder()
                .id(bill.getId())
                .billMonth(bill.getBillMonth())
                .amount(bill.getAmount())
                .generatedDate(bill.getGeneratedDate())
                .dueDate(bill.getDueDate())
                .status(bill.getStatus())
                .residentId(resident != null ? resident.getId() : null)
                .residentName(resident != null ? resident.getName() : null)
                .residentEmail(resident != null ? resident.getEmail() : null)
                .build();
    }
}
