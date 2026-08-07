package com.habitasphere.controller;

import com.habitasphere.dto.DocumentResponse;
import com.habitasphere.dto.DocumentVerificationRequest;
import com.habitasphere.entity.Document;
import com.habitasphere.enums.DocumentStatus;
import com.habitasphere.enums.DocumentType;
import com.habitasphere.repository.DocumentRepository;
import com.habitasphere.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/documents")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN') or hasRole('SECRETARY')")
public class AdminDocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentRepository documentRepository;

    /**
     * Get all uploaded documents
     */
    @GetMapping
    public ResponseEntity<List<DocumentResponse>> getAllDocuments() {

        return ResponseEntity.ok(
                documentService.getAllDocuments()
        );
    }

    /**
     * Get all pending documents
     */
    @GetMapping("/pending")
    public ResponseEntity<List<Document>> getPendingDocuments() {

        return ResponseEntity.ok(
                documentRepository.findByStatus(DocumentStatus.PENDING)
        );
    }

    /**
     * Filter documents by type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Document>> getDocumentsByType(
            @PathVariable DocumentType type
    ) {

        return ResponseEntity.ok(
                documentRepository.findByDocumentType(type)
        );
    }

    /**
     * Verify document
     */
    @PutMapping("/{id}/verify")
    public ResponseEntity<DocumentResponse> verifyDocument(
            @PathVariable Long id,
            @RequestBody(required = false) DocumentVerificationRequest request,
            Authentication authentication
    ) {

        String remarks = "";

        if (request != null) {
            remarks = request.getRemarks();
        }

        return ResponseEntity.ok(
                documentService.verifyDocument(
                        id,
                        remarks,
                        authentication.getName()
                )
        );
    }

    /**
     * Reject document
     */
    @PutMapping("/{id}/reject")
    public ResponseEntity<DocumentResponse> rejectDocument(
            @PathVariable Long id,
            @RequestBody DocumentVerificationRequest request,
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                documentService.rejectDocument(
                        id,
                        request.getRemarks(),
                        authentication.getName()
                )
        );
    }

    /**
     * Search documents by resident id
     */
    @GetMapping("/resident/{residentId}")
    public ResponseEntity<List<Document>> getResidentDocuments(
            @PathVariable Long residentId
    ) {

        return ResponseEntity.ok(
                documentRepository.findByResidentId(residentId)
        );
    }

    /**
     * Search resident documents by type
     */
    @GetMapping("/resident/{residentId}/type/{type}")
    public ResponseEntity<List<Document>> getResidentDocumentsByType(
            @PathVariable Long residentId,
            @PathVariable DocumentType type
    ) {

        return ResponseEntity.ok(
                documentRepository.findByResidentIdAndDocumentType(
                        residentId,
                        type
                )
        );
    }

}