package com.example.sep.repositories;

import com.example.sep.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client getClientByMerchantId(String merchantId);
    List<Client> getClientsByPort(String port);
}
