package com.example.sep.services;

import com.example.sep.models.Client;
import com.example.sep.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService implements IClientService{
    @Autowired
    private ClientRepository clientRepository;
    @Override
    public Client create(Client client) {
        return this.clientRepository.save(client);
    }
}
