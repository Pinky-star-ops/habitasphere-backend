package com.habitasphere.dto;

import com.habitasphere.enums.DocumentStatus;
import com.habitasphere.enums.DocumentType;

import java.time.LocalDateTime;

public class DocumentResponse {

    private Long id;
    private String originalFileName;
    private String storedFileName;
    private String contentType;
    private Long fileSize;
    private String filePath;
    private DocumentType documentType;
    private DocumentStatus status;
    private boolean verified;
    private String remarks;
    private LocalDateTime uploadedAt;
    private LocalDateTime verifiedAt;
    private Long residentId;
    private String residentName;
    private Long verifiedById;
    private String verifiedByName;

    public DocumentResponse() {
    }

    public DocumentResponse(Long id, String originalFileName, String storedFileName,
                            String contentType, Long fileSize, String filePath,
                            DocumentType documentType, DocumentStatus status,
                            boolean verified, String remarks,
                            LocalDateTime uploadedAt, LocalDateTime verifiedAt,
                            Long residentId, String residentName,
                            Long verifiedById, String verifiedByName) {

        this.id = id;
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.filePath = filePath;
        this.documentType = documentType;
        this.status = status;
        this.verified = verified;
        this.remarks = remarks;
        this.uploadedAt = uploadedAt;
        this.verifiedAt = verifiedAt;
        this.residentId = residentId;
        this.residentName = residentName;
        this.verifiedById = verifiedById;
        this.verifiedByName = verifiedByName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getStoredFileName() {
        return storedFileName;
    }

    public void setStoredFileName(String storedFileName) {
        this.storedFileName = storedFileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public Long getResidentId() {
        return residentId;
    }

    public void setResidentId(Long residentId) {
        this.residentId = residentId;
    }

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }

    public Long getVerifiedById() {
        return verifiedById;
    }

    public void setVerifiedById(Long verifiedById) {
        this.verifiedById = verifiedById;
    }

    public String getVerifiedByName() {
        return verifiedByName;
    }

    public void setVerifiedByName(String verifiedByName) {
        this.verifiedByName = verifiedByName;
    }
}