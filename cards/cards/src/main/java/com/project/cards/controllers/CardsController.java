package com.project.cards.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.cards.constants.CardsConstants;
import com.project.cards.dto.CardsContactInfoDto;
import com.project.cards.dto.CardsDto;
import com.project.cards.dto.ResponseDto;
import com.project.cards.services.CardsService;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController 
@RequestMapping (path = "/api", produces= {MediaType.APPLICATION_JSON_VALUE})
@Validated 
public class CardsController {
    
    private static final Logger logger = LoggerFactory.getLogger(CardsController.class);
    private CardsService cardsService;
    private CardsContactInfoDto cardsContactInfoDto;
    
    public CardsController(CardsService cardsService,
        CardsContactInfoDto cardsContactInfoDto
    ){
        this.cardsService = cardsService;
        this.cardsContactInfoDto = cardsContactInfoDto;
    }


    @Value ("${build.version")
    private String buildVersion;

    @PostMapping ("/create")
    public ResponseEntity<ResponseDto> createCard(@Valid @RequestParam 
        String mobileNumber
    ) {
        logger.info("Creating card for mobileNumber: {}", mobileNumber);
        cardsService.createCard(mobileNumber);
        logger.info("Successfully created card for mobileNumber: {}", mobileNumber);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ResponseDto(CardsConstants.STATUS_201, CardsConstants.MESSAGE_201));

    }


    @GetMapping("/fetch")
    public ResponseEntity<CardsDto> fetchCardDetails(
        @Valid @RequestParam String mobileNumber
    ) {
        logger.info("Fetching card details for mobileNumber: {}", mobileNumber);
        CardsDto cardsDto = cardsService.fetchCard(mobileNumber);
        logger.info("Successfully fetched card details for mobileNumber: {}", mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(cardsDto);
    }

    @GetMapping("/update")
    public ResponseEntity<ResponseDto> updateCardDetails(@Valid @RequestParam 
        CardsDto cardsDto
    ) {
        logger.info("Updating card details for cardNumber: {}", cardsDto.getCardNumber());
        boolean isUpdated = cardsService.updateCard(cardsDto);
        if(isUpdated) {
            logger.info("Successfully updated card details for cardNumber: {}", cardsDto.getCardNumber());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
        }else{
            logger.warn("Failed to update card details for cardNumber: {}", cardsDto.getCardNumber());
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_UPDATE));
        }
        
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCardDetails(@RequestParam String mobileNumber) {
        logger.info("Deleting card details for mobileNumber: {}", mobileNumber);
        boolean isDeleted = cardsService.deleteCard(mobileNumber);
        if(isDeleted) {
            logger.info("Successfully deleted card details for mobileNumber: {}", mobileNumber);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
        }else{
            logger.warn("Failed to delete card details for mobileNumber: {}", mobileNumber);
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_DELETE));
        }
    }

    @Bulkhead(name="getBuildInfo", fallbackMethod = "getBuildInfoFallback")
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() throws InterruptedException {
        logger.debug("getBuildInfo() method Invoked");
        Thread.sleep(6000); // simulate a slow call so concurrency limit is easy to hit
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildVersion);
    }

    public ResponseEntity<String> getBuildInfoFallback(Throwable throwable) {
        logger.debug("getBuildInfoFallback() method Invoked");
        return ResponseEntity.status(HttpStatus.OK)
                .body("GetBuildInfo API Fallback - Too many concurrent requests, please try again later!!!");
    }


    @GetMapping("/contact-info")
    public ResponseEntity<CardsContactInfoDto> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cardsContactInfoDto);
    }
}