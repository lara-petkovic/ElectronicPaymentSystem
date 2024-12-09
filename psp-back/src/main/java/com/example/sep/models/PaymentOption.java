package com.example.sep.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "payment_option")
public class PaymentOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "option")
    private String option;

    @ManyToMany(mappedBy = "paymentOptions",fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Client> clients=new HashSet<Client>();

    public PaymentOption(){}

    public PaymentOption(Long id, String option, Set<Client> clients) {
        this.id = id;
        this.option = option;
        this.clients = clients;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
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

    @Override
    public String toString() {
        return option ;
    }
}
