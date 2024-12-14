package com.example.sep.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name="merchantId")
    private String merchantId;
    @Column(name="merchantPass")
    private String merchantPass;
    @NotNull
    @Column(name="port")
    private String port;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(name = "payment_option_client", joinColumns = @JoinColumn(name = "client_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "option_id", referencedColumnName = "id"))
    @JsonIgnore
    private Set<PaymentOption> paymentOptions=new HashSet<PaymentOption>();

    public Set<PaymentOption> getPaymentOptions() {
        return paymentOptions;
    }

    public void setPaymentOptions(Set<PaymentOption> paymentOptions) {
        this.paymentOptions = paymentOptions;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantPass() {
        return merchantPass;
    }

    public void setMerchantPass(String merchantPass) {
        this.merchantPass = merchantPass;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public String getPort() {
        return port;
    }
    public void setPort(String port) {
        this.port = port;
    }

    public void addPaymentOption(PaymentOption e) {
        if(e!=null) {
            e.getClients().add(this);
            e.setClients(e.getClients());
            this.paymentOptions.add(e);
        }
    }

    public void removePaymentOption(PaymentOption e) {
        this.paymentOptions.remove(e);
    }
}
