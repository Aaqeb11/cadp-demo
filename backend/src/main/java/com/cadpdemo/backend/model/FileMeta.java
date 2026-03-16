package com.cadpdemo.backend.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "file_meta")
public class FileMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "encrypted_data", columnDefinition = "BYTEA")
    private byte[] encryptedData;

    @Column(name = "uploaded_at")
    private Instant uploadedAt;

    public Long getId() {
        return id;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String n) {
        this.originalName = n;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String c) {
        this.contentType = c;
    }

    public byte[] getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(byte[] d) {
        this.encryptedData = d;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Instant t) {
        this.uploadedAt = t;
    }
}
