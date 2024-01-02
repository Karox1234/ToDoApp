package com.teamsparta.todoapp.domain.cards.service

import com.teamsparta.todoapp.domain.cards.dto.CardResponse
import com.teamsparta.todoapp.domain.cards.dto.CreateCardRequest
import com.teamsparta.todoapp.domain.cards.dto.UpdateCardRequest

interface CardService {
    fun getAllCardList(): List<CardResponse>

    fun getCardById(cardId: Long): CardResponse

    fun createCard(request: CreateCardRequest): CardResponse

    fun updateCard(cardId: Long, request: UpdateCardRequest): CardResponse

    fun deleteCard(cardId: Long)

    fun toggleCardCompletion(cardId: Long) : CardResponse
}

//완료여부 체크 제작 완료