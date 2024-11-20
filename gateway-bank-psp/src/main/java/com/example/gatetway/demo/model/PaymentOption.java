package com.example.gatetway.demo.model;

import jakarta.persistence.*;

@Entity
public class PaymentOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "option")
    private String option;
    @Column(name = "address")
    private String address;

    public PaymentOption(){}
    public PaymentOption(Long id, String option, String address) {
        this.id = id;
        this.option = option;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
