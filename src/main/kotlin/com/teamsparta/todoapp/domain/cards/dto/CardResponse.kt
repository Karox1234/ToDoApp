package com.teamsparta.todoapp.domain.cards.dto


import java.time.LocalDateTime


data class CardResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val writer: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val userId: Long,
    var completed: Boolean?,
    val imageUrl: String?
)
