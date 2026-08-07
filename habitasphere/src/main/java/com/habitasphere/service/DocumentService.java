package com.habitasphere.service;

import com.habitasphere.dto.DocumentResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {
    
    DocumentResponse uploadDocument(MultipartFile file, String documentType, String username);
    
    List<DocumentResponse> getMyDocuments(String username);
    
    DocumentResponse getDocumentById(Long id, String username);
    
    Resource downloadDocument(Long id, String username);
    
    void deleteDocument(Long id, String username);
    
    List<DocumentResponse> getAllDocuments();
    
    DocumentResponse verifyDocument(Long id, String remarks, String adminUsername);
    
    DocumentResponse rejectDocument(Long id, String remarks, String adminUsername);
}