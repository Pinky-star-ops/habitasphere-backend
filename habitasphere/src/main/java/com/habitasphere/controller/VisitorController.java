package com.habitasphere.controller;

import com.habitasphere.dto.VisitorRequest;
import com.habitasphere.dto.VisitorResponse;
import com.habitasphere.service.VisitorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visitors")
@RequiredArgsConstructor
public class VisitorController {

    private final VisitorService visitorService;

    // SECURITY GUARD CREATES VISITOR ENTRY
    @PostMapping
    @PreAuthorize("hasRole('SECURITY')")
    public VisitorResponse createVisitor(
            @Valid @RequestBody VisitorRequest request) {

        return visitorService.createVisitor(request);
    }

    // RESIDENT APPROVES VISITOR
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('RESIDENT')")
    public VisitorResponse approveVisitor(@PathVariable Long id) {

        return visitorService.approveVisitor(id);
    }

    // RESIDENT REJECTS VISITOR
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('RESIDENT')")
    public VisitorResponse rejectVisitor(@PathVariable Long id) {

        return visitorService.rejectVisitor(id);
    }

    // SECURITY MARKS EXIT
    @PutMapping("/{id}/exit")
    @PreAuthorize("hasRole('SECURITY')")
    public VisitorResponse markExit(@PathVariable Long id) {

        return visitorService.markExit(id);
    }

    // ADMIN GETS ALL VISITORS
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<VisitorResponse> getAllVisitors() {

        return visitorService.getAllVisitors();
    }

    // RESIDENT GETS OWN VISITOR HISTORY
    @GetMapping("/my-visitors")
    @PreAuthorize("hasRole('RESIDENT')")
    public List<VisitorResponse> getMyVisitors() {

        return visitorService.getMyVisitors();
    }
}