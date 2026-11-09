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

    public void createCard(String mobileNo){
        Optional<Cards> cards = cardsRepository.findByMobileNo(mobileNo);
        if(cards.isPresent()){
            throw new CardAlreadyExistsException("Card already registered with given mobile number " + mobileNo);
        }

        cardsRepository.save(createNewCard(mobileNo));
    }

    private Cards createNewCard(String mobileNo){
        Cards newCard = new Cards();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        newCard.setCardNo(Long.toString(randomCardNumber));
        newCard.setMobileNo(mobileNo);
        newCard.setCardType(CardsConstants.CREDIT_CARD);
        newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
        return newCard; 
    }


    public CardsDto fetchCard(String mobileNo){
        Cards cards = cardsRepository.findByMobileNo(mobileNo)
        .orElseThrow(() -> new ResourceNotFoundException("Card", "mobileNo", mobileNo));
        
        return CardsMapper.mapToCardsDto(cards, new CardsDto());
    }

    public boolean updateCard(CardsDto cardsDto){
        Cards cards = cardsRepository.findByCardNo(cardsDto.getCardNo())
        .orElseThrow(() -> new ResourceNotFoundException("Card", "cardNo", cardsDto.getCardNo()));
        
        CardsMapper.mapToCards(cardsDto, cards);
        cardsRepository.save(cards);
        return true;
    }

    public boolean deleteCard(String mobileNo){
        Cards cards = cardsRepository.findByMobileNo(mobileNo)
        .orElseThrow(() -> new ResourceNotFoundException("Card", "mobileNo", mobileNo));
        
        cardsRepository.deleteById(cards.getCardId());
        return true;
    }

}