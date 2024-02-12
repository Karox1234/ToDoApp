package com.teamsparta.todoapp.domain.cards.repository

import com.teamsparta.todoapp.domain.cards.model.Card

interface CustomCardRepository {
//적용한곳 없음, 단순 연습
    fun findCardWithComments(cardId: Long): Card?
}