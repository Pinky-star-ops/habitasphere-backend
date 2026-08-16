package com.habitasphere.service;

import com.habitasphere.dto.StaffDTO;
import com.habitasphere.entity.Society;
import com.habitasphere.entity.Staff;
import com.habitasphere.enums.StaffType;
import com.habitasphere.repository.SocietyRepository;
import com.habitasphere.repository.StaffRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StaffService {

    private final StaffRepository staffRepository;
    private final SocietyRepository societyRepository;

    public StaffService(
            StaffRepository staffRepository,
            SocietyRepository societyRepository
    ) {
        this.staffRepository = staffRepository;
        this.societyRepository = societyRepository;
    }

    public StaffDTO createStaff(StaffDTO dto) {

        validateStaff(dto);

        Society society = societyRepository.findById(dto.getSocietyId())
                .orElseThrow(() -> new RuntimeException("Society not found"));

        Staff staff = new Staff();

        staff.setName(dto.getName());
        staff.setPhone(dto.getPhone());
        staff.setEmail(dto.getEmail());
        staff.setStaffType(dto.getStaffType());
        staff.setJoiningDate(dto.getJoiningDate());
        staff.setSalary(dto.getSalary());
        staff.setAddress(dto.getAddress());
        staff.setIsActive(true);
        staff.setSociety(society);

        Staff savedStaff = staffRepository.save(staff);

        return convertToDTO(savedStaff);
    }

    public List<StaffDTO> getAllStaff() {

        return staffRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public StaffDTO getStaffById(Long id) {

        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        return convertToDTO(staff);
    }

    public List<StaffDTO> getStaffByType(StaffType type) {

        return staffRepository.findByStaffType(type)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public StaffDTO updateStaff(Long id, StaffDTO dto) {

        validateStaff(dto);

        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        if (dto.getSocietyId() != null &&
                !dto.getSocietyId().equals(staff.getSociety().getId())) {

            Society society = societyRepository.findById(dto.getSocietyId())
                    .orElseThrow(() -> new RuntimeException("Society not found"));

            staff.setSociety(society);
        }

        staff.setName(dto.getName());
        staff.setPhone(dto.getPhone());
        staff.setEmail(dto.getEmail());
        staff.setStaffType(dto.getStaffType());
        staff.setJoiningDate(dto.getJoiningDate());
        staff.setSalary(dto.getSalary());
        staff.setAddress(dto.getAddress());

        if (dto.getIsActive() != null) {
            staff.setIsActive(dto.getIsActive());
        }

        Staff updatedStaff = staffRepository.save(staff);

        return convertToDTO(updatedStaff);
    }

    public void deactivateStaff(Long id) {

        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        staff.setIsActive(false);

        staffRepository.save(staff);
    }

    private void validateStaff(StaffDTO dto) {

        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new RuntimeException("Staff name is required");
        }

        if (dto.getPhone() == null || dto.getPhone().trim().isEmpty()) {
            throw new RuntimeException("Staff phone is required");
        }

        if (dto.getStaffType() == null) {
            throw new RuntimeException("Staff type is required");
        }

        if (dto.getSalary() != null && dto.getSalary() < 0) {
            throw new RuntimeException("Salary cannot be negative");
        }

        if (dto.getJoiningDate() != null &&
                dto.getJoiningDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Joining date cannot be in the future");
        }

        if (dto.getSocietyId() == null) {
            throw new RuntimeException("Society ID is required");
        }
    }

    private StaffDTO convertToDTO(Staff staff) {

        StaffDTO dto = new StaffDTO();

        dto.setId(staff.getId());
        dto.setName(staff.getName());
        dto.setPhone(staff.getPhone());
        dto.setEmail(staff.getEmail());
        dto.setStaffType(staff.getStaffType());
        dto.setJoiningDate(staff.getJoiningDate());
        dto.setSalary(staff.getSalary());
        dto.setAddress(staff.getAddress());
        dto.setIsActive(staff.getIsActive());

        if (staff.getSociety() != null) {
            dto.setSocietyId(staff.getSociety().getId());
        }

        return dto;
    }
}