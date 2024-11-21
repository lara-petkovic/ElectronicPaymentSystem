package com.example.bank.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class BankIdentifierNumber {
    @Id
    private String id;

    public BankIdentifierNumber(String id) {
        this.id = id;
    }

    public BankIdentifierNumber() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
