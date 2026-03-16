package com.cadpdemo.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "encrypted_email")
    private byte[] encryptedEmail;

    @Column(name = "encrypted_ssn")
    private byte[] encryptedSsn;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getEncryptedEmail() {
        return encryptedEmail;
    }

    public void setEncryptedEmail(byte[] e) {
        this.encryptedEmail = e;
    }

    public byte[] getEncryptedSsn() {
        return encryptedSsn;
    }

    public void setEncryptedSsn(byte[] s) {
        this.encryptedSsn = s;
    }
}
