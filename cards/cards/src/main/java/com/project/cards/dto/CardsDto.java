package com.project.cards.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data 
public class CardsDto {
    
    @NotEmpty(message = "Mobile Number cannot be a null or empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile Number should be a 10 digit number")
    private String mobileNo;

    @NotEmpty(message = "Card Number can not be a null or empty")
    @Pattern(regexp="(^$|[0-9]{12})",message = "CardNumber must be 12 digits")
    private String cardNo;

    @NotEmpty(message = "Card Type cannot be a null or empty")
    private String cardType;

    @Positive(message = "Total card limit should be greater than zero")
    private int totalLimit;

    @PositiveOrZero (message = "Amount used should be greater than or equal to zero")
    private int amountUsed;

    @PositiveOrZero (message = "Available amount should be greater than or equal to zero")
    private int availableAmount;
}
