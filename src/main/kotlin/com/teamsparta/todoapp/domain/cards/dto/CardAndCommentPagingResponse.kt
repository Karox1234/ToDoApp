package com.teamsparta.todoapp.domain.cards.dto

data class CardAndCommentPagingResponse(
    var content : List<CardAndCommentResponse>,
    val totalPages: Int,
    val totalComments: Long
)
