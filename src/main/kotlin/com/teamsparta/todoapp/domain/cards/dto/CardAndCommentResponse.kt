package com.teamsparta.todoapp.domain.cards.dto

import com.teamsparta.todoapp.domain.comment.dto.CommentResponse
import java.time.LocalDateTime

data class CardAndCommentResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val writer: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val userId: Long,
    var completed: Boolean?,
    val imageUrl: String?,
    var comments: List<CommentResponse>?,
)
