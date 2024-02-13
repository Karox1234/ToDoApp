package com.teamsparta.todoapp.domain.cards.service

import com.teamsparta.todoapp.domain.cards.dto.*
import org.springframework.data.domain.Sort

interface CardService {

    fun getCardPage(pageNumber: Int, pageSize: Int, sortField: String?, sortOrder: Sort.Direction): CardPageResponse

    fun getAllCardList(): List<CardResponse>

    fun getCardById(cardId: Long, page: Int, size: Int): CardAndCommentPagingResponse

    fun createCard(request: CreateCardRequest,userId: Long): CardResponse

    fun updateCard(cardId: Long, userId:Long, request: UpdateCardRequest): CardResponse

    fun deleteCard(cardId: Long,userId: Long)

    fun toggleCardCompletion(cardId: Long,userId: Long): CardResponse

    fun testCardAndCommentGetList(cardId: Long) : CardAndCommentResponse
}

