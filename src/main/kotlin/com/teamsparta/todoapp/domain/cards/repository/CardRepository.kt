package com.teamsparta.todoapp.domain.cards.repository

import com.teamsparta.todoapp.domain.cards.model.Card
import org.springframework.data.jpa.repository.JpaRepository

interface CardRepository : JpaRepository<Card, Long> {
    fun getCardById(cardId: Long): Card
}