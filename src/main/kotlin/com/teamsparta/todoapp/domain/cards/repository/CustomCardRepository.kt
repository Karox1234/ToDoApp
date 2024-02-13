package com.teamsparta.todoapp.domain.cards.repository

import com.teamsparta.todoapp.domain.cards.dto.CardAndCommentPagingResponse
import com.teamsparta.todoapp.domain.cards.model.Card
import org.springframework.data.domain.Pageable

interface CustomCardRepository {

    fun findCardWithComments(cardId: Long): Card?

    fun findCardWithPagedComments(cardId: Long, pageable: Pageable): CardAndCommentPagingResponse
}