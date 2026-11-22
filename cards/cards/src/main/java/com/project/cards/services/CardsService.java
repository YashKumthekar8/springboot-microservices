package com.project.cards.services;

import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.project.cards.constants.CardsConstants;
import com.project.cards.dto.CardsDto;
import com.project.cards.entity.Cards;
import com.project.cards.exception.CardAlreadyExistsException;
import com.project.cards.exception.ResourceNotFoundException;
import com.project.cards.mapper.CardsMapper;
import com.project.cards.repository.CardsRepository;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor 
public class CardsService {

    private static final Logger logger = LoggerFactory.getLogger(CardsService.class);

    private CardsRepository cardsRepository;
    private MeterRegistry meterRegistry;

    public void createCard(String mobileNumber){
        Optional<Cards> optionalCards= cardsRepository.findByMobileNumber(mobileNumber);
        if(optionalCards.isPresent()){
            logger.warn("Card already registered with mobileNumber: {}", mobileNumber);
            throw new CardAlreadyExistsException("Card already registered with given mobileNumber "+mobileNumber);
        }
        Cards newCard = cardsRepository.save(createNewCard(mobileNumber));
        logger.debug("Created new card with cardNumber: {} for mobileNumber: {}",
                newCard.getCardNumber(), mobileNumber);
        meterRegistry.counter("cards.operations", "operation", "created").increment();
    }

    private Cards createNewCard(String mobileNumber){
        Cards newCard = new Cards();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        newCard.setCardNumber(Long.toString(randomCardNumber));
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType(CardsConstants.CREDIT_CARD);
        newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
        return newCard; 
    }


    public CardsDto fetchCard(String mobileNumber){
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber)
        .orElseThrow(() -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber));
        
        return CardsMapper.mapToCardsDto(cards, new CardsDto());
    }

    public boolean updateCard(CardsDto cardsDto){
        Cards cards = cardsRepository.findByCardNumber(cardsDto.getCardNumber())
        .orElseThrow(() -> new ResourceNotFoundException("Card", "cardNumber", cardsDto.getCardNumber()));
        
        CardsMapper.mapToCards(cardsDto, cards);
        cardsRepository.save(cards);
        logger.debug("Updated card with cardNumber: {}", cardsDto.getCardNumber());
        meterRegistry.counter("cards.operations", "operation", "updated").increment();
        return true;
    }

    public boolean deleteCard(String mobileNumber){
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber)
        .orElseThrow(() -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber));
        
        cardsRepository.deleteById(cards.getCardId());
    
        logger.debug("Deleted card with cardNumber: {}", cards.getCardNumber());
        meterRegistry.counter("cards.operations", "operation", "deleted").increment();
        return true;
    }

}