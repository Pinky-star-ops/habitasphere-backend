package com.habitasphere.dto;

public class DocumentVerificationRequest {

    private String remarks;

    public DocumentVerificationRequest() {
    }

    public DocumentVerificationRequest(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}