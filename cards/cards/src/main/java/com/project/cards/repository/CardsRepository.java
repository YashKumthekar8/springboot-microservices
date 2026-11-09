package com.project.cards.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.cards.entity.Cards;


public interface CardsRepository extends JpaRepository<Cards, Long>{
    
    Optional<Cards> findByMobileNo(String mobileNo);

    Optional<Cards> findByCardNo(String cardNo);

}
