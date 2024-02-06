package com.teamsparta.todoapp.domain.cards.dto

data class CardPageResponse(
    val content: List<CardResponse>,
    val totalPages: Int,
    val totalCards: Long
)