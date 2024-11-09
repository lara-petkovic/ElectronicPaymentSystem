package com.example.sep.services;

import com.example.sep.models.Client;
import org.springframework.stereotype.Service;

@Service
public interface IClientService {
    public Client create(Client client);
}
