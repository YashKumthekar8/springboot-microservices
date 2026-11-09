package com.project.cards.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.cards.constants.CardsConstants;
import com.project.cards.dto.CardsContactInfoDto;
import com.project.cards.dto.CardsDto;
import com.project.cards.dto.ResponseDto;
import com.project.cards.services.CardsService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController 
@RequestMapping (path = "/api", produces= {MediaType.APPLICATION_JSON_VALUE})
@Validated 
public class CardsController {
    
    private final CardsService cardsService;
    public CardsController(CardsService cardsService){
        this.cardsService = cardsService;
    }

    @Autowired 
    CardsContactInfoDto cardsContactInfoDto;

    @PostMapping ("/create")
    public ResponseEntity<ResponseDto> createCard(@Valid @RequestParam 
        String mobileNo
    ) {
        cardsService.createCard(mobileNo);
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ResponseDto(CardsConstants.STATUS_201, CardsConstants.MESSAGE_201));
    }


    @GetMapping("/fetch")
    public ResponseEntity<CardsDto> fetchCardDetails(@Valid @RequestParam 
        String mobileNo
    ) {
        CardsDto cardsDto = cardsService.fetchCard(mobileNo);
        return ResponseEntity.status(HttpStatus.OK)
        .body(cardsDto);
    }

    @GetMapping("/update")
    public ResponseEntity<ResponseDto> updateCardDetails(@Valid @RequestParam 
        CardsDto cardsDto
    ) {
        boolean isUpdated = cardsService.updateCard(cardsDto);
        if(isUpdated){
            return ResponseEntity.status(HttpStatus.OK)
            .body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
            .status(HttpStatus.EXPECTATION_FAILED)
            .body(new ResponseDto(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_UPDATE));
        }
        
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCardDetails(@RequestParam String mobileNo) {
        boolean isDeleted = cardsService.deleteCard(mobileNo);
        if(isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_DELETE));
        }
    }


    @GetMapping("/contact-info")
    public ResponseEntity<CardsContactInfoDto> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cardsContactInfoDto);
    }

    
}
