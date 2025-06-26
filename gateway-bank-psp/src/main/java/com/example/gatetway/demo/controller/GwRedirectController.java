package com.example.gatetway.demo.controller;

import com.example.gatetway.demo.model.NewTransactionDto;
import com.example.gatetway.demo.service.PaymentOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/api/payments")
public class GwRedirectController {

    @Autowired
    private PaymentOptionService paymentOptionService;

    public GwRedirectController(PaymentOptionService paymentOptionService){
        this.paymentOptionService = paymentOptionService;
    }


    @PostMapping
    public void redirect (@RequestBody NewTransactionDto newtransaction, @RequestHeader("Payment") String option) throws Exception{
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://localhost:" + paymentOptionService.getAddressByOption(option).getPort() + paymentOptionService.getAddressByOption(option).getAddress();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(newtransaction.toString(), headers);
        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }
}
