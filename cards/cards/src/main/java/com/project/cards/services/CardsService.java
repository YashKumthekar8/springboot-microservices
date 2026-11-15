package com.project.cards.services;

import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.project.cards.constants.CardsConstants;
import com.project.cards.dto.CardsDto;
import com.project.cards.entity.Cards;
import com.project.cards.exception.CardAlreadyExistsException;
import com.project.cards.exception.ResourceNotFoundException;
import com.project.cards.mapper.CardsMapper;
import com.project.cards.repository.CardsRepository;

import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor 
public class CardsService {

    private CardsRepository cardsRepository;

    public void createCard(String mobileNumber){
        Optional<Cards> cards = cardsRepository.findByMobileNumber(mobileNumber);
        if(cards.isPresent()){
            throw new CardAlreadyExistsException("Card already registered with given mobile number " + mobileNumber);
        }

        cardsRepository.save(createNewCard(mobileNumber));
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
        return true;
    }

    public boolean deleteCard(String mobileNumber){
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber)
        .orElseThrow(() -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber));
        
        cardsRepository.deleteById(cards.getCardId());
        return true;
    }

}