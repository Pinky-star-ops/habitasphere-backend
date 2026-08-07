package com.habitasphere.service.impl;

import com.habitasphere.dto.BillRequest;
import com.habitasphere.dto.DashboardStatsResponse;
import com.habitasphere.dto.MaintenanceBillResponse;
import com.habitasphere.entity.MaintenanceBill;
import com.habitasphere.entity.RoleType;
import com.habitasphere.entity.User;
import com.habitasphere.enums.BillStatus;
import com.habitasphere.exception.BadRequestException;
import com.habitasphere.exception.ResourceNotFoundException;
import com.habitasphere.repository.MaintenanceBillRepository;
import com.habitasphere.repository.UserRepository;
import com.habitasphere.service.MaintenanceBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceBillServiceImpl implements MaintenanceBillService {

    private final MaintenanceBillRepository billRepository;
    private final UserRepository userRepository;

    @Override
    public MaintenanceBillResponse generateBill(BillRequest request) {
        if (request == null) {
            throw new BadRequestException("Bill request cannot be null.");
        }

        if (request.getResidentId() == null) {
            List<MaintenanceBillResponse> bills = generateMonthlyBills(request);
            if (!bills.isEmpty()) {
                return bills.get(0);
            }
            throw new BadRequestException("No residents found to generate bills for.");
        }

        User resident = userRepository.findById(request.getResidentId())
                .orElseThrow(() -> new ResourceNotFoundException("Resident not found with ID: " + request.getResidentId()));

        Integer month = request.getMonth() != null ? request.getMonth() : LocalDate.now().getMonthValue();
        Integer year = request.getYear() != null ? request.getYear() : LocalDate.now().getYear();

        if (billRepository.existsByResidentAndMonthAndYear(resident, month, year)) {
            throw new BadRequestException("Bill for this month and year already exists for the resident.");
        }

        Double amount = request.getAmount() != null ? request.getAmount() : 1500.0;
        LocalDate dueDate = request.getDueDate() != null ? LocalDate.parse(request.getDueDate()) : LocalDate.now().plusDays(10);

        MaintenanceBill bill = MaintenanceBill.builder()
                .month(month)
                .year(year)
                .amount(amount)
                .paidAmount(0.0)
                .dueAmount(amount)
                .lateFee(0.0)
                .dueDate(dueDate)
                .status(BillStatus.PENDING)
                .resident(resident)
                .build();

        return toResponse(billRepository.save(bill));
    }

    @Override
    public List<MaintenanceBillResponse> generateMonthlyBills(BillRequest request) {
        Integer month = (request != null && request.getMonth() != null) ? request.getMonth() : LocalDate.now().getMonthValue();
        Integer year = (request != null && request.getYear() != null) ? request.getYear() : LocalDate.now().getYear();
        Double amount = (request != null && request.getAmount() != null) ? request.getAmount() : 1500.0;
        LocalDate dueDate = (request != null && request.getDueDate() != null)
                ? LocalDate.parse(request.getDueDate())
                : LocalDate.now().withDayOfMonth(Math.min(10, LocalDate.now().lengthOfMonth()));

        List<User> residents = userRepository.findAll().stream()
                .filter(u -> u.getRoles() != null && u.getRoles().stream()
                        .anyMatch(r -> r.getName() == RoleType.ROLE_RESIDENT))
                .toList();

        List<MaintenanceBill> generatedBills = new ArrayList<>();

        for (User resident : residents) {
            if (!billRepository.existsByResidentAndMonthAndYear(resident, month, year)) {
                MaintenanceBill bill = MaintenanceBill.builder()
                        .month(month)
                        .year(year)
                        .amount(amount)
                        .paidAmount(0.0)
                        .dueAmount(amount)
                        .lateFee(0.0)
                        .dueDate(dueDate)
                        .status(BillStatus.PENDING)
                        .resident(resident)
                        .build();

                generatedBills.add(billRepository.save(bill));
            }
        }

        return generatedBills.stream().map(this::toResponse).toList();
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
        User resident = userRepository.findById(residentId)
                .orElseThrow(() -> new ResourceNotFoundException("Resident not found with ID: " + residentId));

        return billRepository.findByResidentOrderByYearDescMonthDesc(resident)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public MaintenanceBillResponse markAsPaid(Long billId) {
        MaintenanceBill bill = billRepository.findById(billId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with ID: " + billId));

        double totalAmount = bill.getAmount() + (bill.getLateFee() != null ? bill.getLateFee() : 0.0);
        bill.setStatus(BillStatus.PAID);
        bill.setPaidAmount(totalAmount);
        bill.setDueAmount(0.0);

        return toResponse(billRepository.save(bill));
    }

    @Override
    public List<MaintenanceBillResponse> getDefaulters() {
        return billRepository.findByStatus(BillStatus.OVERDUE)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public MaintenanceBillResponse updateBill(Long billId, BillRequest request) {
        MaintenanceBill bill = billRepository.findById(billId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with ID: " + billId));

        if (request.getAmount() != null) {
            bill.setAmount(request.getAmount());
        }
        if (request.getDueDate() != null) {
            bill.setDueDate(LocalDate.parse(request.getDueDate()));
        }
        if (request.getMonth() != null) {
            bill.setMonth(request.getMonth());
        }
        if (request.getYear() != null) {
            bill.setYear(request.getYear());
        }

        double lateFee = bill.getLateFee() != null ? bill.getLateFee() : 0.0;
        double paidAmount = bill.getPaidAmount() != null ? bill.getPaidAmount() : 0.0;
        double totalDue = (bill.getAmount() + lateFee) - paidAmount;
        bill.setDueAmount(Math.max(0.0, totalDue));

        if (bill.getDueAmount() <= 0) {
            bill.setStatus(BillStatus.PAID);
        } else if (paidAmount > 0) {
            bill.setStatus(BillStatus.PARTIALLY_PAID);
        } else if (bill.getDueDate() != null && bill.getDueDate().isBefore(LocalDate.now())) {
            bill.setStatus(BillStatus.OVERDUE);
        } else {
            bill.setStatus(BillStatus.PENDING);
        }

        return toResponse(billRepository.save(bill));
    }

    @Override
    public List<MaintenanceBillResponse> getMyBills() {
        User resident = getAuthenticatedUser();
        return billRepository.findByResidentOrderByYearDescMonthDesc(resident)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public MaintenanceBillResponse getBillDetails(Long billId) {
        MaintenanceBill bill = billRepository.findById(billId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with ID: " + billId));

        User user = getAuthenticatedUser();

        boolean isAdminOrSecretary = user.getRoles().stream()
                .anyMatch(role -> role.getName() == RoleType.ROLE_ADMIN || role.getName() == RoleType.ROLE_SECRETARY);

        if (!isAdminOrSecretary && !bill.getResident().getId().equals(user.getId())) {
            throw new BadRequestException("You do not have permission to view this bill.");
        }

        return toResponse(bill);
    }

    @Override
    public DashboardStatsResponse getDashboardStats() {
        LocalDate now = LocalDate.now();
        Double totalCollected = billRepository.getTotalCollected();
        Double pendingAmount = billRepository.getPendingAmount();
        Long totalDefaulters = billRepository.getTotalDefaulters();
        Double monthlyRevenue = billRepository.getMonthlyRevenue(now.getMonthValue(), now.getYear());

        return DashboardStatsResponse.builder()
                .totalCollected(totalCollected != null ? totalCollected : 0.0)
                .pendingAmount(pendingAmount != null ? pendingAmount : 0.0)
                .totalDefaulters(totalDefaulters != null ? totalDefaulters : 0L)
                .monthlyRevenue(monthlyRevenue != null ? monthlyRevenue : 0.0)
                .build();
    }

    private MaintenanceBillResponse toResponse(MaintenanceBill bill) {
        User resident = bill.getResident();

        return MaintenanceBillResponse.builder()
                .id(bill.getId())
                .month(bill.getMonth())
                .year(bill.getYear())
                .amount(bill.getAmount())
                .paidAmount(bill.getPaidAmount())
                .dueAmount(bill.getDueAmount())
                .lateFee(bill.getLateFee())
                .generatedAt(bill.getGeneratedAt())
                .dueDate(bill.getDueDate())
                .status(bill.getStatus())
                .residentId(resident != null ? resident.getId() : null)
                .residentName(resident != null ? resident.getName() : null)
                .residentEmail(resident != null ? resident.getEmail() : null)
                .build();
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new BadRequestException("Authenticated user is required.");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + authentication.getName()));
    }
}
