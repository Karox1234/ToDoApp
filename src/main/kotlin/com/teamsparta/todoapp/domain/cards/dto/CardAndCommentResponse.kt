package com.teamsparta.todoapp.domain.cards.dto

import com.teamsparta.todoapp.domain.comment.dto.CommentResponse

data class CardAndCommentResponse(
    val card: CardResponse,
    val comments: List<CommentResponse>
)
