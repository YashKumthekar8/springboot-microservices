package com.project.cards.mapper;

import com.project.cards.dto.CardsDto;
import com.project.cards.entity.Cards;

public class CardsMapper {

    public static CardsDto mapToCardsDto(Cards cards, CardsDto cardsDto) {
        cardsDto.setCardNumber(cards.getCardNumber());
        cardsDto.setCardType(cards.getCardType());
        cardsDto.setMobileNumber(cards.getMobileNumber());
        cardsDto.setTotalLimit(cards.getTotalLimit());
        cardsDto.setAvailableAmount(cards.getAvailableAmount());
        cardsDto.setAmountUsed(cards.getAmountUsed());
        return cardsDto;
    }

    public static Cards mapToCards(CardsDto cardsDto, Cards cards) {
        cards.setCardNumber(cardsDto.getCardNumber());
        cards.setCardType(cardsDto.getCardType());
        cardsDto.setMobileNumber(cardsDto.getMobileNumber());
        cardsDto.setTotalLimit(cardsDto.getTotalLimit());
        cardsDto.setAvailableAmount(cardsDto.getAvailableAmount());
        cardsDto.setAmountUsed(cardsDto.getAmountUsed());
        return cards;
    }

}