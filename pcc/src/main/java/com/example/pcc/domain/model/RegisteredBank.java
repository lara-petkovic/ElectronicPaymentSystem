package com.example.pcc.domain.model;

import jakarta.persistence.*;

@Entity
public class RegisteredBank {
    @Id
    private String id;
    @Column(name="name")
    private String name;
    @Column(name="url")
    private String url;
    public RegisteredBank(){}
    public RegisteredBank(String id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
