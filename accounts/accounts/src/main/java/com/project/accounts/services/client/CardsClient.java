package com.project.accounts.services.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import com.project.accounts.dto.CardsDTO;

@HttpExchange ("/api")
public interface CardsClient {
 
    @GetExchange (value = "/fetch", accept = "application/json")
    ResponseEntity<CardsDTO> fetchCardDetails(
        @RequestParam String mobileNumber);
}
