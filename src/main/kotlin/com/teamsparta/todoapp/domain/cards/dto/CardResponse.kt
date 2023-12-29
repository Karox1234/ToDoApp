package com.teamsparta.todoapp.domain.cards.dto

import java.time.LocalDateTime
import java.time.OffsetDateTime


data class CardResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val writer: String?,
    val createdAT : OffsetDateTime
)
