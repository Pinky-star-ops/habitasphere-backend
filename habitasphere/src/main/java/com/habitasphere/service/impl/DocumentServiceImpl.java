package com.habitasphere.service.impl;

import com.habitasphere.dto.DocumentResponse;
import com.habitasphere.entity.Document;
import com.habitasphere.entity.RoleType;
import com.habitasphere.entity.User;
import com.habitasphere.enums.DocumentStatus;
import com.habitasphere.enums.DocumentType;
import com.habitasphere.exception.BadRequestException;
import com.habitasphere.exception.ResourceNotFoundException;
import com.habitasphere.repository.DocumentRepository;
import com.habitasphere.repository.UserRepository;
import com.habitasphere.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {

    private static final String UPLOAD_DIR = "uploads/documents";
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public DocumentResponse uploadDocument(MultipartFile file, String documentTypeStr, String username) {
        if (file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size exceeds maximum limit of 10MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !(contentType.equalsIgnoreCase("application/pdf") ||
                contentType.equalsIgnoreCase("image/jpeg") ||
                contentType.equalsIgnoreCase("image/jpg") ||
                contentType.equalsIgnoreCase("image/png"))) {
            throw new BadRequestException("Invalid file type. Only PDF, JPG, JPEG, and PNG are allowed");
        }

        DocumentType documentType;
        try {
            documentType = DocumentType.valueOf(documentTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid document type");
        }

        User resident = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFileName = file.getOriginalFilename();
            String extension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String storedFileName = UUID.randomUUID().toString() + extension;
            Path filePath = uploadPath.resolve(storedFileName);

            file.transferTo(filePath.toFile());

            Document document = new Document();
            document.setOriginalFileName(originalFileName);
            document.setStoredFileName(storedFileName);
            document.setContentType(contentType);
            document.setFileSize(file.getSize());
            document.setFilePath(filePath.toString());
            document.setDocumentType(documentType);
            document.setStatus(DocumentStatus.PENDING);
            document.setVerified(false);
            document.setResident(resident);

            Document savedDocument = documentRepository.save(document);
            return mapToDocumentResponse(savedDocument);

        } catch (IOException e) {
            throw new BadRequestException("Failed to store file on disk");
        }
    }

    @Override
    public List<DocumentResponse> getMyDocuments(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return documentRepository.findByResidentId(user.getId()).stream()
                .map(this::mapToDocumentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DocumentResponse getDocumentById(Long id, String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        checkAccess(document, user);

        return mapToDocumentResponse(document);
    }

    @Override
    public Resource downloadDocument(Long id, String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        checkAccess(document, user);

        Path filePath = Paths.get(document.getFilePath()).toAbsolutePath().normalize();
        if (!Files.exists(filePath)) {
            throw new ResourceNotFoundException("File not found on disk");
        }

        return new FileSystemResource(filePath.toFile());
    }

    @Override
    public void deleteDocument(Long id, String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        checkAccess(document, user);

        Path filePath = Paths.get(document.getFilePath()).toAbsolutePath().normalize();
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new BadRequestException("Could not delete file from disk");
        }

        documentRepository.delete(document);
    }

    @Override
    public List<DocumentResponse> getAllDocuments() {
        return documentRepository.findAll().stream()
                .map(this::mapToDocumentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DocumentResponse verifyDocument(Long id, String remarks, String adminUsername) {
        User admin = userRepository.findByEmail(adminUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found"));

        checkAdminOrSecretaryAccess(admin);

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        document.setStatus(DocumentStatus.VERIFIED);
        document.setVerified(true);
        document.setRemarks(remarks);
        document.setVerifiedBy(admin);
        document.setVerifiedAt(LocalDateTime.now());

        return mapToDocumentResponse(documentRepository.save(document));
    }

    @Override
    public DocumentResponse rejectDocument(Long id, String remarks, String adminUsername) {
        User admin = userRepository.findByEmail(adminUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found"));

        checkAdminOrSecretaryAccess(admin);

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        document.setStatus(DocumentStatus.REJECTED);
        document.setVerified(false);
        document.setRemarks(remarks);
        document.setVerifiedBy(admin);
        document.setVerifiedAt(LocalDateTime.now());

        return mapToDocumentResponse(documentRepository.save(document));
    }

    private void checkAccess(Document document, User user) {
        boolean isAdminOrSecretary = user.getRoles().stream()
                .anyMatch(role -> role.getName() == RoleType.ROLE_ADMIN || role.getName() == RoleType.ROLE_SECRETARY);

        if (!isAdminOrSecretary && !document.getResident().getId().equals(user.getId())) {
            throw new BadRequestException("You do not have permission to access this document");
        }
    }

    private void checkAdminOrSecretaryAccess(User user) {
        boolean isAdminOrSecretary = user.getRoles().stream()
                .anyMatch(role -> role.getName() == RoleType.ROLE_ADMIN || role.getName() == RoleType.ROLE_SECRETARY);

        if (!isAdminOrSecretary) {
            throw new BadRequestException("Only Admin or Secretary can perform this action");
        }
    }

    private DocumentResponse mapToDocumentResponse(Document document) {
        DocumentResponse dto = new DocumentResponse();
        dto.setId(document.getId());
        dto.setOriginalFileName(document.getOriginalFileName());
        dto.setStoredFileName(document.getStoredFileName());
        dto.setContentType(document.getContentType());
        dto.setFileSize(document.getFileSize());
        dto.setFilePath(document.getFilePath());
        dto.setDocumentType(document.getDocumentType());
        dto.setStatus(document.getStatus());
        dto.setVerified(document.isVerified());
        dto.setRemarks(document.getRemarks());
        dto.setUploadedAt(document.getUploadedAt());
        dto.setVerifiedAt(document.getVerifiedAt());

        if (document.getResident() != null) {
            dto.setResidentId(document.getResident().getId());
            dto.setResidentName(document.getResident().getName());
        }

        if (document.getVerifiedBy() != null) {
            dto.setVerifiedById(document.getVerifiedBy().getId());
            dto.setVerifiedByName(document.getVerifiedBy().getName());
        }

        return dto;
    }
}
