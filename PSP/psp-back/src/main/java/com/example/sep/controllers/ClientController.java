package com.example.sep.controllers;

import com.example.sep.configuration.ClientSubscriptionWebSocketHandler;
import com.example.sep.dtos.NewClientDto;
import com.example.sep.dtos.PaymentOptionDto;
import com.example.sep.models.PaymentOption;
import com.example.sep.services.IClientService;
import com.example.sep.services.PaymentOptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/subscription")

public class ClientController {
    @Autowired
    private PaymentOptionService paymentOptionService;
    @Autowired
    private IClientService clientService;
    private final ClientSubscriptionWebSocketHandler clientSubscriptionWebSocketHandler;
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    public ClientController(ClientSubscriptionWebSocketHandler clientSubscriptionWebSocketHandler, PaymentOptionService paymentOptionService, IClientService clientService) {
        this.clientSubscriptionWebSocketHandler = clientSubscriptionWebSocketHandler;
        this.paymentOptionService=paymentOptionService;
        this.clientService=clientService;
    }
    @PostMapping
    public ResponseEntity<NewClientDto> CreateClient(@RequestBody NewClientDto newClient) throws Exception {
        if(clientService.getClientByPort(newClient.apiKey)!=null){

        }
        List<PaymentOption> paymentOptionList = paymentOptionService.getAll();
        StringBuilder message= new StringBuilder(newClient.apiKey + ",");
        for(int i=0;i<paymentOptionList.size()-1;i++){
            message.append(paymentOptionList.get(i).getOption()).append(" ");
        }
        message.append(paymentOptionList.get(paymentOptionList.size() - 1).getOption());
        clientSubscriptionWebSocketHandler.broadcastMessage(message.toString());
        logger.info("New client registration request, port: "+newClient.getApiKey());
        return new ResponseEntity<>(newClient, HttpStatus.CREATED) ;
    }
    @GetMapping("/{port}")
    public ResponseEntity<List<PaymentOptionDto>> GetByClient (@PathVariable String port){
        if(clientService.getClientByPort(port)==null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(clientService.getOptionsForClient(port),HttpStatus.OK);
    }
    @PutMapping("/{port}")
    public ResponseEntity<String> RemoveOption(@PathVariable String port, @RequestBody PaymentOptionDto option){
        if(clientService.getClientByPort(port)==null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        clientService.RemoveOption(port, option.getName());
        logger.info("Client on port "+port+" removed option "+option);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
