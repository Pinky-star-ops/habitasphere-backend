package com.habitasphere.controller;

import com.habitasphere.dto.DocumentResponse;
import com.habitasphere.dto.DocumentUploadResponse;
import com.habitasphere.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "*")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    /**
     * Upload a new document
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentResponse> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("documentType") String documentType,
            Authentication authentication
    ) {

        String username = authentication.getName();

        DocumentResponse response =
                documentService.uploadDocument(
                        file,
                        documentType,
                        username
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Get logged-in resident documents
     */
    @GetMapping("/my")
    public ResponseEntity<List<DocumentResponse>> getMyDocuments(
            Authentication authentication
    ) {

        String username = authentication.getName();

        List<DocumentResponse> documents =
                documentService.getMyDocuments(username);

        return ResponseEntity.ok(documents);
    }

    /**
     * Get single document details
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> getDocument(
            @PathVariable Long id,
            Authentication authentication
    ) {

        String username = authentication.getName();

        DocumentResponse response =
                documentService.getDocumentById(
                        id,
                        username
                );

        return ResponseEntity.ok(response);
    }

    /**
     * Download document
     */
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadDocument(
            @PathVariable Long id,
            Authentication authentication
    ) {

        String username = authentication.getName();

        Resource resource =
                documentService.downloadDocument(
                        id,
                        username
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(resource.getFilename())
                                .build()
                                .toString()
                )
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
        /**
     * Delete a document
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDocument(
            @PathVariable Long id,
            Authentication authentication
    ) {

        String username = authentication.getName();

        documentService.deleteDocument(
                id,
                username
        );

        return ResponseEntity.ok("Document deleted successfully.");
    }

    /**
     * Check Document Module Status
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Document Management Module is running successfully.");
    }

}