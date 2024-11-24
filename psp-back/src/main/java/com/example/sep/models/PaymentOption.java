package com.example.sep.models;

import jakarta.persistence.*;

@Entity
@Table(name = "payment_option")
public class PaymentOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "option")
    private String option;

    public PaymentOption(){}
    public PaymentOption(Long id, String option) {
        this.id = id;
        this.option = option;
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
}
