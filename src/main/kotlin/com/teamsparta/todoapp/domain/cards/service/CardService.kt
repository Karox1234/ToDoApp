package com.teamsparta.todoapp.domain.cards.service

import com.teamsparta.todoapp.domain.cards.dto.CardAndCommentResponse
import com.teamsparta.todoapp.domain.cards.dto.CardResponse
import com.teamsparta.todoapp.domain.cards.dto.CreateCardRequest
import com.teamsparta.todoapp.domain.cards.dto.UpdateCardRequest

interface CardService {
    fun getAllCardList(): List<CardResponse>

    fun getCardById(cardId: Long): CardResponse

    fun getCommentsByCardId(cardId: Long): CardAndCommentResponse

    fun createCard(request: CreateCardRequest): CardResponse

    fun updateCard(cardId: Long, request: UpdateCardRequest): CardResponse

    fun deleteCard(cardId: Long)

    fun toggleCardCompletion(cardId: Long): CardResponse


}

