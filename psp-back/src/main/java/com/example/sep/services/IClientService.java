package com.example.sep.services;

import com.example.sep.dtos.ClientAuthenticationDataDto;
import com.example.sep.models.Client;
import org.springframework.stereotype.Service;

@Service
public interface IClientService {
    public ClientAuthenticationDataDto create(Client client);
}
