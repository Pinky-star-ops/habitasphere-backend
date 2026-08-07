package com.habitasphere.repository;

import com.habitasphere.entity.Document;
import com.habitasphere.enums.DocumentStatus;
import com.habitasphere.enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByResidentId(Long residentId);

    List<Document> findByStatus(DocumentStatus status);

    List<Document> findByDocumentType(DocumentType documentType);

    List<Document> findByResidentIdAndDocumentType(
            Long residentId,
            DocumentType documentType
    );

}