package com.project.cards.mapper;

import com.project.cards.dto.CardsDto;
import com.project.cards.entity.Cards;

public class CardsMapper {

    public static CardsDto mapToCardsDto(Cards cards, CardsDto cardsDto) {
        cardsDto.setCardNo(cards.getCardNo());
        cardsDto.setCardType(cards.getCardType());
        cardsDto.setMobileNo(cards.getMobileNo());
        cardsDto.setTotalLimit(cards.getTotalLimit());
        cardsDto.setAvailableAmount(cards.getAvailableAmount());
        cardsDto.setAmountUsed(cards.getAmountUsed());
        return cardsDto;
    }

    public static Cards mapToCards(CardsDto cardsDto, Cards cards) {
        cards.setCardNo(cardsDto.getCardNo());
        cards.setCardType(cardsDto.getCardType());
        cards.setMobileNo(cardsDto.getMobileNo());
        cards.setTotalLimit(cardsDto.getTotalLimit());
        cards.setAvailableAmount(cardsDto.getAvailableAmount());
        cards.setAmountUsed(cardsDto.getAmountUsed());
        return cards;
    }

}
