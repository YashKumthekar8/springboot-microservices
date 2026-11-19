package com.project.accounts.services.client;

import org.springframework.cloud.client.circuitbreaker.httpservice.HttpServiceFallback;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.project.accounts.dto.CardsDTO;

@Component 
@HttpServiceFallback(value=CardsFallback.class, group = "cards")
public class CardsFallback {

    public ResponseEntity<CardsDTO> fetchCardDetails(String correlationId, String mobileNumber){
        return null;
    }


    
}
