package com.habitasphere.scheduler;

import com.habitasphere.entity.MaintenanceBill;
import com.habitasphere.entity.RoleType;
import com.habitasphere.entity.User;
import com.habitasphere.enums.BillStatus;
import com.habitasphere.repository.MaintenanceBillRepository;
import com.habitasphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MaintenanceScheduler {

    private final MaintenanceBillRepository billRepository;
    private final UserRepository userRepository;

    // Run on the 1st of every month at midnight
    @Scheduled(cron = "0 0 0 1 * ?")
    public void generateMonthlyBills() {
        log.info("Starting monthly maintenance bill generation...");
        
        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();

        Double defaultAmount = 1500.0;
        LocalDate dueDate = today.withDayOfMonth(Math.min(10, today.lengthOfMonth()));

        List<User> residents = userRepository.findAll().stream()
                .filter(u -> u.getRoles() != null && u.getRoles().stream()
                        .anyMatch(r -> r.getName() == RoleType.ROLE_RESIDENT))
                .toList();

        int generatedCount = 0;

        for (User resident : residents) {
            if (!billRepository.existsByResidentAndMonthAndYear(resident, currentMonth, currentYear)) {
                MaintenanceBill bill = MaintenanceBill.builder()
                        .month(currentMonth)
                        .year(currentYear)
                        .amount(defaultAmount)
                        .paidAmount(0.0)
                        .dueAmount(defaultAmount)
                        .lateFee(0.0)
                        .dueDate(dueDate)
                        .status(BillStatus.PENDING)
                        .resident(resident)
                        .build();

                billRepository.save(bill);
                generatedCount++;
            }
        }

        log.info("Successfully generated {} maintenance bills for {}/{}", generatedCount, currentMonth, currentYear);
    }

    // Run every day at 1 AM to check for overdue bills and add late fees
    @Scheduled(cron = "0 0 1 * * ?")
    public void checkOverdueBills() {
        log.info("Checking for overdue maintenance bills...");
        
        LocalDate today = LocalDate.now();

        // Find bills past due date that are not PAID
        List<MaintenanceBill> overdueBills = billRepository.findByDueDateBeforeAndStatusNot(today, BillStatus.PAID);
        
        int markedCount = 0;

        for (MaintenanceBill bill : overdueBills) {
            if (bill.getStatus() != BillStatus.OVERDUE) {
                bill.setStatus(BillStatus.OVERDUE);
                
                // Add late fee if it was just marked overdue
                if (bill.getLateFee() == null || bill.getLateFee() == 0.0) {
                    double lateFeeAmount = 100.0;
                    bill.setLateFee(lateFeeAmount);
                    double currentDue = bill.getDueAmount() != null ? bill.getDueAmount() : (bill.getAmount() - (bill.getPaidAmount() != null ? bill.getPaidAmount() : 0.0));
                    bill.setDueAmount(currentDue + lateFeeAmount);
                }
                
                billRepository.save(bill);
                markedCount++;
            }
        }

        log.info("Successfully marked {} bills as overdue.", markedCount);
    }
}
